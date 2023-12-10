package com.appservices.booking.mapstruct.mappers;

import com.appservices.booking.data.dto.*;
import com.appservices.booking.data.entity.*;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(
        componentModel = "spring"
)
public interface MapStructMapper {

    BookingDTO bookingToBookingDTO(Booking booking);
    BookedTimeslotDTO bookedTimeslotToBookedTimeslotDTO(BookedTimeslot bookedTimeslot);
    MeetingRoomDTO meetingRoomToMeetingRoomDTO(MeetingRoom meetingRoom);
    List<MeetingRoomDTO> meetingRoomToMeetingRoomDTO(List<MeetingRoom> meetingRooms);
    TimeSlotDTO timeSlotToTimeSlotDTO(TimeSlot timeSlot);
    Set<TimeSlotDTO> timeSlotToTimeSlotDTO(Set<TimeSlot> timeSlots);
    BookingUserDTO bookingUserToBookingUserDTO(BookingUser bookingUser);
}
