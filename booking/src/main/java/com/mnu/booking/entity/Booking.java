package com.mnu.booking.entity;

import java.time.LocalDateTime;

import javax.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
	name = "BOOKING_SEQ_GENERATOR"
    , sequenceName = "BOOKING_SEQ"
    , initialValue = 1
    , allocationSize = 1
)
@Table(name = "booking")
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOKING_SEQ_GENERATOR")
    @Column(nullable = false)
    private Integer bno;

    @ManyToOne
    @JoinColumn(name = "fno")
    private Facility facility;

    @ManyToOne
    @JoinColumn(name = "sid")
    private Student student;

    @Column(name="start_time", nullable = false)
    private LocalDateTime startTime;
 
    @Column(name="end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private Integer headcount;
}