package com.appservices.booking.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;

@Data
@EqualsAndHashCode
@Entity
@Table(name = "time_slot")
public class TimeSlot {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;
    @Column
    LocalTime startTime;
    @Column
    LocalTime endTime;
    @Column
    Boolean maintenance;

}
