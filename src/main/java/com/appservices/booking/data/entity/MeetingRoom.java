package com.appservices.booking.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@Entity
@Table(name = "meeting_room")
public class MeetingRoom {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;
    String roomName;
    Integer capacity;
}
