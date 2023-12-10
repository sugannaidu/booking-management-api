package com.appservices.booking.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MeetingRoomDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("roomName")
    private String roomName;
    @JsonProperty("capacity")
    private Integer capacity;
}
