package com.mnu.booking.entity;

import javax.persistence.*;

import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
	name = "FACILITY_SEQ_GENERATOR"
    , sequenceName = "FACILITY_SEQ"
    , initialValue = 1
    , allocationSize = 1
)
@Table(name = "facility")
public class Facility {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FACILITY_SEQ_GENERATOR")
    private Integer fno;

    @Column(length = 20, nullable = false)
    private String name;
 
    @Column(length = 20, nullable = false)
    private String place;
    
    
    @Column(length = 13, nullable = false)
    private String scale;
}
