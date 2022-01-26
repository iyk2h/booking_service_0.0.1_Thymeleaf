package com.mnu.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

import com.mnu.booking.dto.*;
import com.mnu.booking.entity.*;
//Entity <-> DTO

@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);
    
    AdminDTO adminToDTO(Admin admin);
    Admin adminToEntity(AdminDTO adminDTO);
    AdminInfoDTO adminToInfoDTO(Admin admin);

    BookingDTO bookingToDTO(Booking booking);
    Booking bookingToEntity(BookingDTO bookingDTO); 
    List<BookingDTO> bookingToDTOList(List<Booking> bookingList);

    FacilityDTO facilityToDTO(Facility facility);
    Facility facilityToEntity(FacilityDTO facilityDTO);
    List<FacilityDTO> facilityToDTOList(List<Facility> facilityList);

    ManageDTO manageToDTO(Manage manage);
    Manage manageToEntity(ManageDTO manageDTO);
    List<ManageDTO> manageToDTOList(List<Manage> manageList);

    StudentDTO studentToDTO(Student student);
    Student studentsToEntity(StudentDTO studentDTO);
    StudentInfoDTO studentToInfoDTO(Student student);
}
