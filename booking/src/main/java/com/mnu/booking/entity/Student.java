package com.mnu.booking.entity;

import javax.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "student")
public class Student {
    @Id
    @Column(length = 20, nullable = false)
    private String sid;

    @Column(length = 20, nullable = false)
    private String pw;
 
    @Column(length = 20, nullable = false)
    private String name;
    
    @Column(length = 13, nullable = false)
    private String phone;
}