package com.mnu.booking.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mnu.booking.entity.*;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class BookingDTO {
    private Integer bno;
    private Facility facility;
    private Student student;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime endTime;

    private Integer headcount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    
    private Integer btnradio;
}
