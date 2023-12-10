package com.appservices.booking.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class TimeSlotDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("startTime")
    private LocalTime startTime;
    @JsonProperty("endTime")
    private LocalTime endTime;
    @JsonProperty("maintenance")
    private Boolean maintenance;
}
