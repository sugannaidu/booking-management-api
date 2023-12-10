package com.appservices.booking.service;

import com.appservices.booking.data.dto.BookingDTO;
import com.appservices.booking.data.dto.MeetingRoomDTO;
import com.appservices.booking.data.entity.Booking;
import com.appservices.booking.data.entity.MeetingRoom;

import java.util.List;

public interface BookingService {
    BookingDTO bookConferenceRoom(String startTime, String endTime, Integer noOfPeople);
    List<MeetingRoomDTO> checkAvailableRooms(String startTime, String endTime);
}
