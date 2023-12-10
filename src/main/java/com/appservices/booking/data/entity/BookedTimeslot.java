package com.appservices.booking.data.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "booked_time_slot")
public class BookedTimeslot {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    //@OneToOne(cascade = CascadeType.ALL, mappedBy = "booked_time_slot")
    @OneToOne(cascade = CascadeType.ALL)
    MeetingRoom meetingRoom;

    //@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //@Transient
    @ManyToMany(fetch = FetchType.EAGER)
    Set<TimeSlot> usedTimeSlots = new HashSet<>();

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "booking_id", referencedColumnName = "id")
//    private Booking booking;
}
