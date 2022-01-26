package com.mnu.booking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import com.mnu.booking.dto.*;
import com.mnu.booking.entity.*;
import com.mnu.booking.mapper.BookingMapper;
import com.mnu.booking.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManageService {
    @Autowired
	private ManageRepository manageRepository;
	
	//관리 정보
	public Manage getManage(Integer mno) {
		Optional<Manage> manageOpt = manageRepository.findById(mno);
		Manage manage = manageOpt.orElse(null);

		return manage;
	}

    //관리 리스트
    public List<Manage> getManageList() {
		List<Manage> manageList = manageRepository.findAll();

		return manageList;
	}
	
    //특정 시설의 관리 리스트
	public List<Manage> getManageListOfFacility(Facility facility) {
		LocalDateTime date = LocalDateTime.now();
		
		List<Manage> manageList = manageRepository.findAllByFacility(facility, date);

		return manageList;
	}

	//특정 관리자의 관리 리스트
	public List<Manage> getManageListOfAdmin(Admin admin) {
		List<Manage> manageList  = manageRepository.findAllByAdmin(admin);

		return manageList;
	}

    //관리 추가 / 업데이트
	public Manage saveManage(ManageDTO manageDTO) {
		Manage manage = BookingMapper.INSTANCE.manageToEntity(manageDTO);
		
		return manageRepository.save(manage);
	}
	
    //관리 삭제
	public void deleteManage(Integer mno) {
		manageRepository.deleteById(mno);
	}
}
