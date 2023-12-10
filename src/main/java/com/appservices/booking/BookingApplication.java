package com.appservices.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@ComponentScan(basePackageClasses = {Booking.class, BookedTimeslot.class, BookingUser.class, MeetingRoom.class, TimeSlot.class})
@SpringBootApplication
public class BookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingApplication.class, args);
	}

}
