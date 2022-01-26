package com.mnu.booking.service;

import java.util.List;
import java.util.Optional;

import com.mnu.booking.dto.*;
import com.mnu.booking.entity.*;
import com.mnu.booking.mapper.BookingMapper;
import com.mnu.booking.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacilityService {
    @Autowired
	private FacilityRepository facilityRepository;
	@Autowired
	BookingRepository bookingRepository;
	@Autowired
	ManageRepository manageRepository;
	
    //시설들 반환
    public List<Facility> getFacilityList() {
		List<Facility> facilityList = facilityRepository.findAllSorted();

		return facilityList;
	}
	
    //1개의 시설 정보
	public Facility getFacility(Integer fno) {
		Optional<Facility> facilityOpt = facilityRepository.findById(fno);
		Facility facility = facilityOpt.orElse(null);

		return facility;
	}

    //시설 추가 / 업데이트
	public Facility saveFacility(FacilityDTO facilityDTO) {
		Facility facility = BookingMapper.INSTANCE.facilityToEntity(facilityDTO);
		
		return facilityRepository.save(facility);
	}
	
    //시설 삭제
	public void deleteFacility(Facility facility) {
		List<Booking> bookingList  = bookingRepository.findByFacility(facility);
		bookingRepository.deleteAll(bookingList);

		List<Manage> manageList = manageRepository.findByFacility(facility);
		manageRepository.deleteAll(manageList);

		facilityRepository.deleteById(facility.getFno());
	}
}
