package com.mnu.booking.repository;

import java.util.List;

import com.mnu.booking.entity.*;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface FacilityRepository extends CrudRepository<Facility, Integer> {
    @Query(value="SELECT f FROM Facility f ORDER BY f.fno DESC")
    List<Facility> findAllSorted();
}
