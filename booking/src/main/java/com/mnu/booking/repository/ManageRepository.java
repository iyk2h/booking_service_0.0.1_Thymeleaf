package com.mnu.booking.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.mnu.booking.entity.*;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ManageRepository extends CrudRepository<Manage, Integer> {
    @Override
    List<Manage> findAll();

    List<Manage> findAllByAdmin(Admin admin);

    List<Manage> findByAdmin(Admin admin);
    List<Manage> findByFacility(Facility facility);


    /*
        s                  e
        <------------------>
    c1            c2            c3

    현재시간이 c1, c2일때는 관리가 안끝났으므로 보여줘야함

    c1 <= s c1 <= e
    s <= c2 c2 <= e
    현재 시간 <= endTime 이면 보여주면 됨
    */
    @Query(value = "SELECT m FROM Manage m WHERE m.facility = ?1 AND ?2 <= m.endTime ORDER BY m.startTime")
    List<Manage> findAllByFacility(Facility facility, LocalDateTime date);
}
