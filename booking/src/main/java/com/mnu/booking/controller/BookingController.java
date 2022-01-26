package com.mnu.booking.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
@RequestMapping(path="/booking")
public class BookingController {
	@Autowired
	BookingService bookingService;

	@Autowired
	FacilityService facilityService;

	@Autowired
	StudentService studentService;

	@Autowired
	ManageService manageService;

	
    //메인 화면
	@GetMapping(path="")
	public String main() {
		return "booking/main";
	}
	
    //예약할 시설 선택 페이지
	@GetMapping(path="/facility")
	public String facility(Model model) {
		List<Facility> facilityList = facilityService.getFacilityList();
		List<FacilityDTO> facilityDTOList = BookingMapper.INSTANCE.facilityToDTOList(facilityList);

		model.addAttribute("facilityList", facilityDTOList);

		return "booking/facility";
	}

	//예약 페이지
	@GetMapping(path="/booking/{fno}")
	public String booking(@PathVariable("fno") Integer fno, Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String sid = (String)session.getAttribute("sid");

		if (sid == null) {
			model.addAttribute("msg", "로그인 후 이용해주세요.");
        	model.addAttribute("url", "/student/login");

			return "alert";
		}

		Facility facility = facilityService.getFacility(fno);
		String page;

		if (facility == null) {
			model.addAttribute("msg", "잘못된 접근입니다.");
        	model.addAttribute("url", "/facility");

			page = "alert";
		} else {
			model.addAttribute("fno", fno);

			List<Manage> manageList = manageService.getManageListOfFacility(facility);
			List<ManageDTO> manageDTOList = BookingMapper.INSTANCE.manageToDTOList(manageList);

			model.addAttribute("manageList", manageDTOList);

			page = "booking/booking";
		}
		
		return page;
	}

	//특정 날짜의 예약들 JSON
	@GetMapping(path="/booking/getDateList")
	public @ResponseBody List<BookingDTO> booking(@RequestParam("fno") Integer fno, @RequestParam("date") String dateString, Model model) {
		List<Booking> bookingList;
		LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);

		bookingList = bookingService.getBookingListOfDate(date);

		List<BookingDTO> bookingDTOList = BookingMapper.INSTANCE.bookingToDTOList(bookingList);

		return bookingDTOList;
	}

	//예약 처리
	@PostMapping(path="/booking/{fno}")
	public String booking(@PathVariable("fno") Integer fno, @ModelAttribute BookingDTO bookingDTO, Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String sid = (String)session.getAttribute("sid");

		if (sid == null) {
			model.addAttribute("msg", "로그인 후 이용해주세요.");
        	model.addAttribute("url", "/student/login");

			return "alert";
		}

		Student student = studentService.getStudent(sid);
		Facility facility = facilityService.getFacility(fno);

		if (facility == null || student == null) {
			model.addAttribute("msg", "잘못된 접근입니다.");
        	model.addAttribute("url", "/booking/facility");

			return "alert";
		}

		LocalDateTime start = LocalDateTime.of(bookingDTO.getDate(), LocalTime.of(bookingDTO.getBtnradio(), 0));
		LocalDateTime end = start.plusHours(1);

		bookingDTO.setStartTime(start);
		bookingDTO.setEndTime(end);
		bookingDTO.setFacility(facility);
		bookingDTO.setStudent(student);

		Booking booking = bookingService.saveBooking(bookingDTO);

		if (booking == null) {
			model.addAttribute("msg", "이미 예약된 시간입니다.");
        	model.addAttribute("url", "/booking/booking/" + fno);

			return "alert";
		}
		
		return "redirect:/student/mybooking";
	}


	//예약 취소
	@GetMapping(path="/cancel/{bno}")
	public String cancel(@PathVariable("bno") Integer bno, Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String sid = (String)session.getAttribute("sid");

		if (sid == null) {
			model.addAttribute("msg", "로그인 후 이용해주세요.");
			model.addAttribute("url", "/student/login");

			return "alert";
		}

		Booking booking = bookingService.getBooking(bno);

		if (booking == null) {
			model.addAttribute("msg", "잘못된 접근입니다.");
		} else {
			bookingService.deleteBooking(bno);
			model.addAttribute("msg", "취소되었습니다.");
		}
		
		model.addAttribute("url", "/student/mybooking");
		return "alert";
	}
}
