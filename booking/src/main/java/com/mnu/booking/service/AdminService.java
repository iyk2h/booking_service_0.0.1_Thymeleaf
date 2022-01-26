package com.mnu.booking.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.mnu.booking.dto.*;
import com.mnu.booking.entity.*;
import com.mnu.booking.mapper.BookingMapper;
import com.mnu.booking.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class AdminService {
    @Autowired
	private AdminRepository adminRepository;
	@Autowired
	ManageRepository manageRepository;

	//권한 체크
	public boolean checkAdmin(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Boolean admin = (Boolean)session.getAttribute("admin");
		boolean result;

		if (admin == null) {
			model.addAttribute("msg", "권한이 없습니다.");
			model.addAttribute("url", "/booking");
			result = false;
		} else {
			result = true;
		}
		
		return result;
	}

    //로그인
    //일치시 Admin 반환
    //불일치시 null 반환
    public Admin login(LoginDTO loginDTO) {
		Optional<Admin> adminOpt = adminRepository.findById(loginDTO.getId());
		Admin admin;

		if (adminOpt.isEmpty())
			return null;

		admin = adminOpt.get();

        if (!admin.getPw().equals(loginDTO.getPw()))
            admin = null;

		return admin;
	}
	
    //관리자 정보 불러오기
	public Admin getAdmin(String id) {
		Optional<Admin> adminOpt = adminRepository.findById(id);
		Admin admin = adminOpt.orElse(null);

		return admin;
	}

    //관리자 추가 / 업데이트
	public Admin saveAdmin(AdminDTO adminDTO) {
		Admin admin = BookingMapper.INSTANCE.adminToEntity(adminDTO);
		
		return adminRepository.save(admin);
	}
	
    //관리자 삭제
	public void deleteAdmin(Admin admin) {

		List<Manage> manageList = manageRepository.findByAdmin(admin);
		manageRepository.deleteAll(manageList);

		adminRepository.deleteById(admin.getId());
	}
}
