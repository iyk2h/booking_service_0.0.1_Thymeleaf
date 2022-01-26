package com.mnu.booking.repository;

import com.mnu.booking.entity.*;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Admin, String> {
}
