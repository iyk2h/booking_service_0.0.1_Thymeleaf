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
public class StudentService {
    @Autowired
	private StudentRepository studentRepository;
	@Autowired
	BookingRepository bookingRepository;
	
    //로그인
    //일치시 Student 반환
    //불일치시 null 반환
    public Student login(LoginDTO loginDTO) {
		Optional<Student> studentOpt = studentRepository.findById(loginDTO.getId());
		Student student;

		if (studentOpt.isEmpty())
			return null;

		student = studentOpt.get();

        if (!student.getPw().equals(loginDTO.getPw()))
            student = null;

		return student;
	}
	
    //학생 정보 불러오기
	public Student getStudent(String sid) {
		Optional<Student> studentOpt = studentRepository.findById(sid);
		Student student = studentOpt.orElse(null);

		return student;
	}

    //학생 추가 / 업데이트
	public Student saveStudent(StudentDTO studentDTO) {
		Student student = BookingMapper.INSTANCE.studentsToEntity(studentDTO);
		
		return studentRepository.save(student);
	}
	
    //학생 삭제
	public void deleteStudent(Student student) {
		List<Booking> bookingList  = bookingRepository.findAllByStudent(student);
		bookingRepository.deleteAll(bookingList);

		studentRepository.deleteById(student.getSid());
	}
}
