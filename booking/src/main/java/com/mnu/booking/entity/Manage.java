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
	name = "MANAGE_SEQ_GENERATOR"
    , sequenceName = "MANAGE_SEQ"
    , initialValue = 1
    , allocationSize = 1
)
@Table(name = "manage")
public class Manage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MANAGE_SEQ_GENERATOR")
    private Integer mno;

    @ManyToOne
    @JoinColumn(name = "id")
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "fno")
    private Facility facility;

    @Column(name="start_time", nullable = false, unique = true)
    private LocalDateTime startTime;
 
    @Column(name="end_time", nullable = false)
    private LocalDateTime endTime;;
    
    @Column(length = 20)
    private String reason;
}