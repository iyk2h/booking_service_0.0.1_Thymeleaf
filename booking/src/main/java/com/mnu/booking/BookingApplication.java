package com.mnu.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
TO-DO
관리 리스트 보여주기 O
시간이 지난 예약/관리 안보여주기 O
메인 페이지 /admin -> /manage O

현재 시간 이전의 예약 비활성화 O

동시성 제어
- 동시에 들어오면 먼저 들어온 쪽이 락을 걸고 처리 X
-> 1시간 간격으로만 할 것이므로 UNIQUE 키로 변경 O

예약후 관리가 추가되어 관리시간대에 있을 경우 예약 제거 or 취소선으로 보여주기
삭제가 더 쉬움 O

Date -> LocalDateTime 으로 변경 O

*/


@SpringBootApplication
public class BookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingApplication.class, args);
	}

}
