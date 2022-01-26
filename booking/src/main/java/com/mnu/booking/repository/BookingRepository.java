package com.mnu.booking.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.mnu.booking.entity.*;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends CrudRepository<Booking, Integer> {
    @Override
    List<Booking> findAll();

    List<Booking> findAllByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    List<Booking> findAllByStudent(Student student);

    List<Booking> findByFacility(Facility facility);

    /*
        s                  e
        <------------------>
    c1            c2            c3

    예약은 현재 시간이 c1 일때만 보여줘야 함
    현재 시간 <= startTime 이면 보여주면 됨
    */
    @Query(value = "SELECT b FROM Booking b WHERE b.student = ?1 AND ?2 <= b.startTime ORDER BY b.bno")
    List<Booking> findAllByStudent(Student student, LocalDateTime date);

    @Query(value = "SELECT MIN(b.startTime) FROM Booking b WHERE ?1 <= b.startTime")
    LocalDateTime findByMinStartTime(LocalDateTime time);

    @Query(value = "SELECT MAX(b.endTime) FROM Booking b WHERE b.endTime <= ?1")
    LocalDateTime findByMaxEndTime(LocalDateTime time);
    
    void deleteByFacilityAndStartTimeBetween(Facility facility, LocalDateTime startTime, LocalDateTime endTime);
}
