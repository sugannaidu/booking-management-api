package com.appservices.booking.data.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Entity
@Data
@Table(name = "booking")
public class Booking {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;
    //BookingUser bookingUser;
    Integer numberOfPeople;
    LocalTime startTime;
    LocalTime endTime;
//    @OneToOne(cascade = CascadeType.ALL, mappedBy = "booking")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "booking_timeslot_id", referencedColumnName = "id")
    BookedTimeslot bookedTimeslot; //need bidirectional relationship to get booking information from booked time slots
}
