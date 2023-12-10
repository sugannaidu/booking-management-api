package com.appservices.booking.service.impl;

import com.appservices.booking.data.dto.BookingDTO;
import com.appservices.booking.data.dto.MeetingRoomDTO;
import com.appservices.booking.data.entity.BookedTimeslot;
import com.appservices.booking.data.entity.Booking;
import com.appservices.booking.data.entity.MeetingRoom;
import com.appservices.booking.data.entity.TimeSlot;
import com.appservices.booking.data.repository.BookedTimeslotRepository;
import com.appservices.booking.data.repository.BookingRepository;
import com.appservices.booking.data.repository.MeetingRoomRepository;
import com.appservices.booking.data.repository.TimeSlotRepository;
import com.appservices.booking.exception.MaintenanceOverlapException;
import com.appservices.booking.exception.RoomAvailabilityException;
import com.appservices.booking.exception.RoomCapacityException;
import com.appservices.booking.exception.RoomMaxAvailabilityException;
import com.appservices.booking.mapstruct.mappers.MapStructMapper;
import com.appservices.booking.service.BookingService;
import com.appservices.booking.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class BookingServiceImpl implements BookingService {
    public static final String MSG_BOOKING_INTERVAL_OVERLAPS_MAINTENANCE_TIMESLOT = "Booking interval overlaps maintenance timeslot. ";
    public static final String MSG_NO_MEETING_ROOMS_AVAILABLE_FOR_BOOKING = "No meeting rooms available for booking. ";
    public static final String MSG_NO_MEETING_ROOMS_AVAILABLE_TO_ACCOMMODATE_REQUIRED_NUMBER_OF_PEOPLE = "No meeting rooms available to accommodate required number of people.";
    public static final String MSG_NO_OF_PEOPLE_LESS_THAN_2_BELOW_ALLOWED_THRESHOLD = "No of people less than 2, below allowed threshold.";
    Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    TimeSlotRepository timeSlotRepository;
    @Autowired
    MeetingRoomRepository meetingRoomRepository;
    @Autowired
    BookedTimeslotRepository bookedTimeslotRepository;
    @Autowired
    private MapStructMapper mapstructMapper;
    @Override
    public BookingDTO bookConferenceRoom(String startTime, String endTime, Integer noOfPeople) {
        if (noOfPeople <= 1) {
            logger.atInfo().log("<Validation Exception> Minimum number of people should be more than 1 ... " + noOfPeople);
            throw new RoomCapacityException(MSG_NO_OF_PEOPLE_LESS_THAN_2_BELOW_ALLOWED_THRESHOLD);
        }

        List<MeetingRoom> meetingRooms = meetingRoomRepository.findByCapacityGreaterThanEqual(noOfPeople);
        meetingRooms.sort(Comparator.comparingInt(MeetingRoom::getCapacity));
        if (meetingRooms.isEmpty()) {
            logger.atInfo().log("<Validation Exception> No meeting rooms available to accommodate required number of people. ..." + noOfPeople);
            throw new RoomMaxAvailabilityException(MSG_NO_MEETING_ROOMS_AVAILABLE_TO_ACCOMMODATE_REQUIRED_NUMBER_OF_PEOPLE);
        }

        //Timeslots for specific period ... everything booked
        Set<TimeSlot> requiredTimeslotsForBooking = timeSlotRepository.findTimeslotsByStartTimeAndEndTime(
                DateUtils.get24HourTime(startTime), DateUtils.get24HourTime(endTime));
        requiredTimeslotsForBooking.forEach(timeSlot -> {
            if (timeSlot.getMaintenance()) {
                logger.atInfo().log("<Validation Exception> Booking interval overlaps maintenance timeslot ..." + timeSlot);
                throw new MaintenanceOverlapException(MSG_BOOKING_INTERVAL_OVERLAPS_MAINTENANCE_TIMESLOT);
            }
            logger.atInfo().log("All timeslots per range ..." + timeSlot);
        });

        AtomicReference<Optional<MeetingRoom>> selectedMeetingRoom = new AtomicReference<>(Optional.empty());
        for (MeetingRoom meetingRoom : meetingRooms) {
            logger.atInfo().log("order read from list ..." + meetingRoom);
            List<BookedTimeslot> meetingRoomBookedTimeslots = bookedTimeslotRepository.findAllBookedMeetingRooms(meetingRoom.getId());
            //Check meeting rooms with no booking
            if (meetingRoomBookedTimeslots.isEmpty()) {
                selectedMeetingRoom.set(Optional.of(meetingRoom));
                logger.atInfo().log("Selecting meeting room with no booked timeslots ..." + meetingRoom);
                break;
            }

            //Check Booked meeting rooms with Available Timeslots
            for (BookedTimeslot bookedTimeslot1 : meetingRoomBookedTimeslots) {
                long count = requiredTimeslotsForBooking.stream()
                        .filter(bt -> {
                            return bookedTimeslot1.getUsedTimeSlots().stream().anyMatch(bt::equals);
                        })
                        .peek(timeSlot -> logger.atInfo().log("Found meeting rooms with booked timeslots ..." + timeSlot)
                        ).count();
                if (count == 0) {
                    selectedMeetingRoom.set(Optional.of(bookedTimeslot1.getMeetingRoom()));
                    logger.atInfo().log("Selecting meeting room with available timeslots ..." + bookedTimeslot1.getMeetingRoom());
                }
            }
        }

        Optional<MeetingRoom> optionalMeetingRoom = selectedMeetingRoom.get();
        MeetingRoom meetingRoom = optionalMeetingRoom
                .orElseThrow(() -> new RoomAvailabilityException(MSG_NO_MEETING_ROOMS_AVAILABLE_FOR_BOOKING));

        //create booked time slots
        BookedTimeslot bookedTimeslot = new BookedTimeslot();
        bookedTimeslot.setMeetingRoom(meetingRoom);
        bookedTimeslot.setUsedTimeSlots(new HashSet<>(requiredTimeslotsForBooking));
        BookedTimeslot savedBookedTimeslot = bookedTimeslotRepository.save(bookedTimeslot);
        //create booking
        Booking newBooking = new Booking();
        newBooking.setStartTime(DateUtils.get24HourTime(startTime));
        newBooking.setEndTime(DateUtils.get24HourTime(endTime));
        newBooking.setNumberOfPeople(noOfPeople);
        newBooking.setBookedTimeslot(savedBookedTimeslot);
        Booking savedBooking = bookingRepository.save(newBooking);
        return mapstructMapper.bookingToBookingDTO(savedBooking);
    }

    @Override
    public List<MeetingRoomDTO> checkAvailableRooms(String startTime, String endTime) {
        Set<TimeSlot> requiredTimeslotsForAvailabilityCheck = timeSlotRepository.findTimeslotsByStartTimeAndEndTime(
                DateUtils.get24HourTime(startTime), DateUtils.get24HourTime(endTime));
        requiredTimeslotsForAvailabilityCheck.forEach(timeSlot -> {
            if (timeSlot.getMaintenance()) {
                logger.atInfo().log("<Validation Exception> Booking interval overlaps maintenance timeslot ..." + timeSlot);
                throw new MaintenanceOverlapException(MSG_BOOKING_INTERVAL_OVERLAPS_MAINTENANCE_TIMESLOT);
            }
            logger.atInfo().log("All timeslots per range ..." + timeSlot);
        });

        //Add rooms with no booked timeslot
        List<MeetingRoom> availableMeetingRooms = new ArrayList<>();

        //Find Booked meeting rooms with Available Timeslots
        List<BookedTimeslot> allBookedTimeslots = bookedTimeslotRepository.findAllBookedTimeslots();
        allBookedTimeslots.forEach(bookedTimeslot1 -> {

            long count = requiredTimeslotsForAvailabilityCheck.stream()
                    .filter(bt -> {
                        return bookedTimeslot1.getUsedTimeSlots().stream().anyMatch(bt::equals);
                    })
                    .peek(timeSlot -> logger.atInfo().log("Found meeting rooms with booked timeslots ..." + timeSlot)
                    ).count();
            if (count == 0) {
                availableMeetingRooms.add(bookedTimeslot1.getMeetingRoom());
                logger.atInfo().log("Adding booked meeting rooms with available timeslots ..." + bookedTimeslot1.getMeetingRoom());
            }
        });

        // Find unbooked meeting rooms
        Iterable<MeetingRoom> allMeetingRooms = meetingRoomRepository.findAll();
        allMeetingRooms.forEach(am -> {
            long count = allBookedTimeslots.stream().filter(bt -> {
                return am.equals(bt.getMeetingRoom());
            }).count();
            if (count == 0) {
                availableMeetingRooms.add(am);
                logger.atInfo().log("Adding available meeting rooms ..." + am);
            }
        });

        availableMeetingRooms.forEach(m -> logger.atInfo().log("Available meeting rooms ..." + m));

        return mapstructMapper.meetingRoomToMeetingRoomDTO(availableMeetingRooms);
    }
}
