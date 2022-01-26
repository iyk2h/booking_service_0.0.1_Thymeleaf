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
@RequestMapping(path="/student")
public class StudentController {
	@Autowired
	StudentService studentService;

	@Autowired
	BookingService bookingService;

    //로그인 화면
	@GetMapping(path="/login")
	public String login() {
		return "student/login";
	}

    //로그인 처리
    //참고
    //redirect:/alert 일때
    //url http://localhost:8080/alert
    //뷰 alert

    //그냥 alert일때
    //url /http://localhost:8080/login
    //뷰 alert
    @PostMapping(path="/login")
	public String login(@ModelAttribute LoginDTO loginDTO,
                        Model model,
                        HttpServletRequest request) {
		
		Student student = studentService.login(loginDTO);
		String page;
		
		if (student == null) {
			model.addAttribute("msg", "아이디 또는 비밀번호가 틀립니다.");
			model.addAttribute("url", "/student/login");
			page = "alert";
		} else {
			HttpSession session = request.getSession();
			session.setAttribute("sid", student.getSid());
			session.setAttribute("name", student.getName());
			
			page = "redirect:/booking";
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
		
		return "student/signup";
	}

    //회원가입 처리
	@PostMapping(path="/signup")
	public String signup(@ModelAttribute StudentDTO studentDTO, Model model) {
		boolean exist = studentService.getStudent(studentDTO.getSid()) != null;
		
		if (exist == true) {
			model.addAttribute("msg", "회원가입에 실패하였습니다.");
			model.addAttribute("url", "/student/signup");
		} else {
			studentService.saveStudent(studentDTO);
			model.addAttribute("msg", "회원가입에 성공하였습니다.");
			model.addAttribute("url", "/student/login");
		}
		
		return "alert";
	}
	
	//아이디 체크 처리
	@GetMapping(path="/checkID/{id}")
	public @ResponseBody boolean checkID(@PathVariable("id") String id, Model model) {
		Student student = studentService.getStudent(id);
		boolean result = student == null;
		
		return result;
	}
 
    //내 정보 페이지
	@GetMapping(path="/mypage")
	public String mypage(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String sid = (String)session.getAttribute("sid");
		
		String page;

		if (sid == null) {
			model.addAttribute("msg", "로그인 후 이용하세요.");
			model.addAttribute("url", "/student/login");
			page = "alert";
		} else {
			Student student = studentService.getStudent(sid);
			model.addAttribute("student", BookingMapper.INSTANCE.studentToInfoDTO(student));
			page = "student/mypage";
		}
		
		return page;
	}

	//내 예약 페이지
	@GetMapping(path="/mybooking")
	public String mybooking(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String sid = (String)session.getAttribute("sid");
		
		String page;

		if (sid == null) {
			model.addAttribute("msg", "로그인 후 이용하세요.");
			model.addAttribute("url", "/student/login");
			page = "alert";
		} else {
			Student student = studentService.getStudent(sid);
			List<Booking> bookingList = bookingService.getBookingListOfStudent(student);
			List<BookingDTO> bookingDTOList = BookingMapper.INSTANCE.bookingToDTOList(bookingList);

			model.addAttribute("bookingList", bookingDTOList);
			page = "student/mybooking";
		}
		
		return page;
	}

    //내 정보 수정 페이지
    @GetMapping("/update")
	public String update(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String sid = (String)session.getAttribute("sid");
		
		String page;

		if (sid == null) {
			model.addAttribute("msg", "로그인 후 이용하세요.");
			model.addAttribute("url", "/student/login");
			page = "alert";
		} else {
			Student student = studentService.getStudent(sid);
			model.addAttribute("student", BookingMapper.INSTANCE.studentToInfoDTO(student));
			page = "student/update";
		}
		
		return page;
	}
	
    //내 정보 수정 처리
	@PostMapping(path="/update")
	public String update(@ModelAttribute StudentDTO studentDTO, Model model, HttpServletRequest request) {
		//다른 사람인지 체크
		HttpSession session = request.getSession();
		String sid = (String)session.getAttribute("sid");
		
		if (sid.equals(studentDTO.getSid())) {
			Student student = studentService.saveStudent(studentDTO);
			
			if (student == null) {
				model.addAttribute("msg", "정보 수정에 실패하였습니다.");
			} else {
				model.addAttribute("msg", "정보 수정에 성공하였습니다.");
			}
		} else {
			model.addAttribute("msg", "잘못된 접근입니다.");
		}

		model.addAttribute("url", "/student/mypage");
		
		return "alert";
	}

    //탈퇴 처리
    @GetMapping("/delete")
	public String delete(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String sid = (String)session.getAttribute("sid");
		Student student = studentService.getStudent(sid);
		
        if (sid == null) {
            model.addAttribute("msg", "잘못된 접근입니다.");
        	model.addAttribute("url", "/booking");
        } else {
            studentService.deleteStudent(student);
		
            session.invalidate();

            model.addAttribute("msg", "탈퇴 되었습니다.");
	    	model.addAttribute("url", "/booking");
        }

		return "alert";
	}
}
