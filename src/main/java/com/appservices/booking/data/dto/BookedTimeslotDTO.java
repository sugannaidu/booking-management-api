package com.appservices.booking.data.dto;

import com.appservices.booking.data.entity.MeetingRoom;
import com.appservices.booking.data.entity.TimeSlot;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
public class BookedTimeslotDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("meetingRoom")
    private MeetingRoom meetingRoom;
    @JsonProperty("usedTimeSlots")
    private Set<TimeSlot> usedTimeSlots;
}
