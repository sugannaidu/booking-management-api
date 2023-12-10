package com.appservices.booking.data.dto;

import com.appservices.booking.data.entity.BookedTimeslot;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Builder
public class BookingDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("startTime")
    private LocalTime startTime;
    @JsonProperty("endTime")
    private LocalTime endTime;
    @JsonProperty("numberOfPeople")
    private Integer numberOfPeople;
    @JsonProperty("bookedTimeslot")
    private BookedTimeslotDTO bookedTimeslot;
}
