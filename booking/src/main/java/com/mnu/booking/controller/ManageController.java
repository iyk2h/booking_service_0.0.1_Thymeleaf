package com.mnu.booking.controller;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.servlet.http.*;

import com.mnu.booking.dto.*;
import com.mnu.booking.entity.*;
import com.mnu.booking.mapper.BookingMapper;
import com.mnu.booking.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/manage")
public class ManageController {
	@Autowired
	AdminService adminService;

	@Autowired
	FacilityService facilityService;

    @Autowired
	ManageService manageService;

    @Autowired
	BookingService bookingService;

    //메인 화면
	@GetMapping(path="")
	public String main(Model model, HttpServletRequest request) {
		if (adminService.checkAdmin(model, request) == false)
			return "alert";
		
		return "admin/main";
	}

    //시설 목록
    @GetMapping(path="/facility")
    public String facility(Model model, HttpServletRequest request) {
        if (adminService.checkAdmin(model, request) == false)
            return "alert";

        List<Facility> facilityList = facilityService.getFacilityList();
        List<FacilityDTO> facilityDTOList = BookingMapper.INSTANCE.facilityToDTOList(facilityList);

        model.addAttribute("facilityList", facilityDTOList);

        return "manage/facility";
    }

    //시설 추가
    @GetMapping(path="/facility/add")
    public String addFacility(Model model, HttpServletRequest request) {
        if (adminService.checkAdmin(model, request) == false)
            return "alert";

        return "manage/addFacility";
    }
 
    //시설 추가 처리
    @PostMapping(path="/facility/add")
    public String addFacility(FacilityDTO facilityDTO, Model model, HttpServletRequest request) {
        if (adminService.checkAdmin(model, request) == false)
            return "alert";

        facilityService.saveFacility(facilityDTO);

        return "redirect:/manage/facility";
    }

    //시설 수정
    @GetMapping(path="/facility/update/{fno}")
    public String updateFacility(@PathVariable("fno") Integer fno, Model model, HttpServletRequest request) {
        if (adminService.checkAdmin(model, request) == false)
            return "alert";

        Facility facility = facilityService.getFacility(fno);
        FacilityDTO facilityDTO = BookingMapper.INSTANCE.facilityToDTO(facility);
        model.addAttribute("facility", facilityDTO);
        return "manage/editFacility";
    }

    //시설 수정 처리
    @PostMapping(path="/facility/update/{fno}")
    public String updateFacility(@PathVariable("fno") Integer fno, @ModelAttribute FacilityDTO facilityDTO, Model model, HttpServletRequest request) {
        if (adminService.checkAdmin(model, request) == false)
            return "alert";

        facilityDTO.setFno(fno);
        Facility facility = facilityService.saveFacility(facilityDTO);
        if (facility == null) {
            model.addAttribute("msg", "정보 수정에 실패하였습니다.");
            model.addAttribute("url", "/manage/facility/update/" + facilityDTO.getFno());
        } else {
            model.addAttribute("msg", "정보 수정에 성공하였습니다.");
            model.addAttribute("url", "/manage/facility");
        }

        return "alert";
    }

    //시설 제거
    @GetMapping(path="/facility/delete/{fno}")
    public String deleteFacility(@PathVariable("fno") Integer fno, Model model, HttpServletRequest request) {
        if (adminService.checkAdmin(model, request) == false)
            return "alert";

        Facility facility = facilityService.getFacility(fno);

        if (facility == null) {
            model.addAttribute("msg", "잘못된 접근입니다.");
        } else {
            facilityService.deleteFacility(facility);

            model.addAttribute("msg", "삭제되었습니다.");
        }

        model.addAttribute("url", "/manage/facility");
        return "alert";
    }

    //시설 관리
    @GetMapping(path="/manage/{fno}")
    public String manageFacility(@PathVariable("fno") Integer fno, Model model, HttpServletRequest request) {
        if (adminService.checkAdmin(model, request) == false)
            return "alert";

        Facility facility = facilityService.getFacility(fno);
        String page;

        //없는 시설인 경우
        if (facility == null) {
            model.addAttribute("msg", "잘못된 접근입니다.");
            model.addAttribute("url", "/manage/facility");
            page = "alert";
        } else {
            model.addAttribute("fno", fno);

            List<Manage> manageList = manageService.getManageListOfFacility(facility);
			List<ManageDTO> manageDTOList = BookingMapper.INSTANCE.manageToDTOList(manageList);

			model.addAttribute("manageList", manageDTOList);

            page = "manage/manage";
        }

        return page;
    }

    //관리 처리
	@PostMapping(path="/manage/{fno}")
	public String manageFacility(@PathVariable("fno") Integer fno, @ModelAttribute ManageDTO manageDTO, Model model, HttpServletRequest request) {
		if (adminService.checkAdmin(model, request) == false)
            return "alert";
        
        HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");

		if (id == null) {
			model.addAttribute("msg", "로그인 후 이용해주세요.");
        	model.addAttribute("url", "/admin/login");

			return "alert";
		}

		Admin admin = adminService.getAdmin(id);
		Facility facility = facilityService.getFacility(fno);

		if (facility == null || admin == null) {
			model.addAttribute("msg", "잘못된 접근입니다.");
        	model.addAttribute("url", "/admin/facility");

			return "alert";
		}

        manageDTO.setAdmin(admin);
        manageDTO.setFacility(facility);

        // 11-11 00:00 -> 11-11 23:59
        LocalDateTime startTime = LocalDateTime.of(manageDTO.getStart(), LocalTime.of(0, 0));
        LocalDateTime endTime = LocalDateTime.of(manageDTO.getEnd(), LocalTime.of(0, 0));

        endTime = endTime.plusDays(1);
        endTime = endTime.minusMinutes(1);

        System.out.println(endTime);
        manageDTO.setStartTime(startTime);
        manageDTO.setEndTime(endTime);

        Manage manage = manageService.saveManage(manageDTO);
		bookingService.deleteBookingRange(facility, manageDTO);

		return "redirect:/admin/mymanage";
	}

    //한 시설 관리 목록 JSON
	@GetMapping(path="/manage/getManageList")
	public @ResponseBody List<ManageDTO> getManageList(@RequestParam("fno") Integer fno, Model model) {
        Facility facility = facilityService.getFacility(fno);
        List<Manage> manageList = manageService.getManageListOfFacility(facility);
        List<ManageDTO> manageDTOList = BookingMapper.INSTANCE.manageToDTOList(manageList);

		return manageDTOList;
	}

    //관리 취소
	@GetMapping(path="/cancel/{mno}")
	public String cancel(@PathVariable("mno") Integer mno, Model model, HttpServletRequest request) {
        if (adminService.checkAdmin(model, request) == false)
            return "alert";

		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");

		if (id == null) {
			model.addAttribute("msg", "로그인 후 이용해주세요.");
			model.addAttribute("url", "/student/login");

			return "alert";
		}

		Manage manage = manageService.getManage(mno);

		if (manage == null) {
			model.addAttribute("msg", "잘못된 접근입니다.");
		} else {
			manageService.deleteManage(mno);
			model.addAttribute("msg", "취소되었습니다.");
		}
		
		model.addAttribute("url", "/admin/mymanage");
		return "alert";
	}
}