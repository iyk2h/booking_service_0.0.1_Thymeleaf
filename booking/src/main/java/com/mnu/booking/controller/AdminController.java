package com.mnu.booking.controller;

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
@RequestMapping(path="/admin")
public class AdminController {
	@Autowired
	AdminService adminService;

	@Autowired
	FacilityService facilityService;

	@Autowired
	ManageService manageService;

	//로그인 화면 [기본 접근]
	//회원가입 화면 [어드민 코드 있어야 접근]
	//나머지 화면 [어드민 로그인시 접근]

    //로그인 화면
	@GetMapping(path="/login")
	public String login() {
		return "admin/login";
	}

    //로그인 처리
    @PostMapping(path="/login")
	public String login(@ModelAttribute LoginDTO loginDTO,
                        Model model,
                        HttpServletRequest request) {
		
		Admin admin = adminService.login(loginDTO);
		String page;
		
		if (admin == null) {
			model.addAttribute("msg", "아이디 또는 비밀번호가 틀립니다.");
			model.addAttribute("url", "/admin/login");
			page = "alert";
		} else {
			HttpSession session = request.getSession();
			session.setAttribute("admin", true);
			session.setAttribute("id", admin.getId());
			session.setAttribute("name", admin.getName());
			
			page = "redirect:/manage";
		}
		
		return page;
	}
	
    //로그아웃
	@GetMapping(path="/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		session.invalidate();
		
		return "redirect:/booking";
	}
	
    //회원가입 페이지
	@GetMapping(path="/signup")
	public String signup(Model model) {
		
		return "admin/signup";
	}

    //회원가입 처리
	@PostMapping(path="/signup")
	public String signup(@ModelAttribute AdminDTO adminDTO, Model model) {
		boolean exist = adminService.getAdmin(adminDTO.getId()) != null;
		
		if (exist == true) {
			model.addAttribute("msg", "회원가입에 실패하였습니다.");
			model.addAttribute("url", "/admin/signup");
		} else {
			adminService.saveAdmin(adminDTO);
			model.addAttribute("msg", "회원가입에 성공하였습니다.");
			model.addAttribute("url", "/admin/login");
		}
		
		return "alert";
	}
	
	//아이디 체크 처리
	@GetMapping(path="/checkID/{id}")
	public @ResponseBody boolean checkID(@PathVariable("id") String id, Model model) {
		Admin admin = adminService.getAdmin(id);
		boolean result = admin == null;
		
		return result;
	}
 
    //내 정보 페이지
	@GetMapping(path="/mypage")
	public String mypage(Model model, HttpServletRequest request) {
		if (adminService.checkAdmin(model, request) == false)
			return "alert";

		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		
		String page;

		if (id == null) {
			model.addAttribute("msg", "로그인 후 이용하세요.");
			model.addAttribute("url", "/admin/login");
			page = "alert";
		} else {
			Admin admin = adminService.getAdmin(id);
			model.addAttribute("admin", BookingMapper.INSTANCE.adminToInfoDTO(admin));
			page = "admin/mypage";
		}
		
		return page;
	}

    //내 정보 수정 페이지
    @GetMapping("/update")
	public String update(Model model, HttpServletRequest request) {
		if (adminService.checkAdmin(model, request) == false)
			return "alert";

		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		String page;

		if (id == null) {
			model.addAttribute("msg", "로그인 후 이용하세요.");
			model.addAttribute("url", "/admin/login");
			page = "alert";
		} else {
			Admin admin = adminService.getAdmin(id);
			model.addAttribute("admin", BookingMapper.INSTANCE.adminToInfoDTO(admin));
			page = "admin/update";
		}
		
		return page;
	}
	
    //내 정보 수정 처리
	@PostMapping(path="/update")
	public String update(@ModelAttribute AdminDTO adminDTO, Model model, HttpServletRequest request) {
		if (adminService.checkAdmin(model, request) == false)
			return "alert";

		//다른 사람인지 체크
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		
		if (id.equals(adminDTO.getId())) {
			Admin admin = adminService.saveAdmin(adminDTO);
			
			if (admin == null) {
				model.addAttribute("msg", "정보 수정에 실패하였습니다.");
			} else {
				model.addAttribute("msg", "정보 수정에 성공하였습니다.");
			}
		} else {
			model.addAttribute("msg", "잘못된 접근입니다.");
		}
		
		model.addAttribute("url", "/admin/mypage");
		
		return "alert";
	}

	//내 관리 페이지
	@GetMapping(path="/mymanage")
	public String mybooking(Model model, HttpServletRequest request) {
		if (adminService.checkAdmin(model, request) == false)
			return "alert";

		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		
		String page;

		if (id == null) {
			model.addAttribute("msg", "로그인 후 이용하세요.");
			model.addAttribute("url", "/admin/login");
			page = "alert";
		} else {
			Admin admin = adminService.getAdmin(id);
			List<Manage> manageList = manageService.getManageListOfAdmin(admin);
			List<ManageDTO> manageDTOList = BookingMapper.INSTANCE.manageToDTOList(manageList);

			model.addAttribute("manageList", manageDTOList);
			page = "admin/mymanage";
		}
		
		return page;
	}

    //탈퇴 처리
    @GetMapping("/delete")
	public String delete(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		
        if (id == null) {
            model.addAttribute("msg", "잘못된 접근입니다.");
        	model.addAttribute("url", "/booking");
        } else {
			adminService.deleteAdmin(adminService.getAdmin(id));
		
            session.invalidate();

            model.addAttribute("msg", "탈퇴 되었습니다.");
	    	model.addAttribute("url", "/booking");
        }

		return "alert";
	}
}
