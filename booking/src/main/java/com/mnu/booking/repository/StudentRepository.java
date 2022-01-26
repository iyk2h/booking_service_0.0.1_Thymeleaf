package com.mnu.booking.repository;


import com.mnu.booking.entity.*;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, String> {

}
