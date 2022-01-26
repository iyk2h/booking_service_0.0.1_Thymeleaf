package com.mnu.booking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.mnu.booking.dto.*;
import com.mnu.booking.entity.*;
import com.mnu.booking.mapper.BookingMapper;
import com.mnu.booking.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    @Autowired
	private BookingRepository bookingRepository;
	
	//예약 정보
	public Booking getBooking(Integer bno) {
		Optional<Booking> bookingOpt = bookingRepository.findById(bno);
		Booking booking = bookingOpt.orElse(null);

		return booking;
	}

    //예약 리스트
    public List<Booking> getBookingList() {
		List<Booking> bookingList = bookingRepository.findAll();

		return bookingList;
	}
	
    //특정 날의 예약 리스트
	public List<Booking> getBookingListOfDate(LocalDate date) {
		LocalDateTime start = LocalDateTime.of(date, LocalTime.of(0, 0));
		LocalDateTime end = start.plusDays(1);

		List<Booking> bookingList = bookingRepository.findAllByStartTimeBetween(start, end);

		return bookingList;
	}

	//특정 학생의 예약 리스트
	//시작시간이 아직 안지난 예약만 보여줌
	public List<Booking> getBookingListOfStudent(Student student) {
		LocalDateTime date = LocalDateTime.now();
		List<Booking> bookingList  = bookingRepository.findAllByStudent(student, date);

		return bookingList;
	}

    //예약 추가 / 업데이트
	/*
	s1      e1     s2    e2    s3      e3
	<------->      <----->     <------->
	          <-->
			  sk ek
	
	조건 1)
	sk 보다 큰 si중 가장 작은 값을 smin

	ek 보다 작은 ei중 가장 큰 값을 emax

	emax <= sk && ek <= smin을 만족하면 됨

	다만 여기서는 무조건 1시간 간격으로 할 것이므로
	start_time을 유니크키로 하면 중복 INSERT를 막을 수 있다.
	*/
	public Booking saveBooking(BookingDTO bookingDTO) {
		Booking booking = BookingMapper.INSTANCE.bookingToEntity(bookingDTO);
		
		Booking result;

		try {
			result = bookingRepository.save(booking);
		} catch (Exception e) {
			result = null;
		}

		return result;
	}
	
    //예약 삭제
	public void deleteBooking(Integer bno) {
		bookingRepository.deleteById(bno);
	}

	//예약 범위 삭제
	@Transactional
	public void deleteBookingRange(Facility facility, ManageDTO manageDTO) {
		bookingRepository.deleteByFacilityAndStartTimeBetween(facility, manageDTO.getStartTime(), manageDTO.getEndTime());
	}
}
