package com.appservices.booking.service.impl;

import com.appservices.booking.data.entity.MeetingRoom;
import com.appservices.booking.data.entity.TimeSlot;
import com.appservices.booking.data.repository.BookedTimeslotRepository;
import com.appservices.booking.data.repository.BookingRepository;
import com.appservices.booking.data.repository.MeetingRoomRepository;
import com.appservices.booking.data.repository.TimeSlotRepository;
import com.appservices.booking.service.BookSystemConfigurator;
import com.appservices.booking.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class BookingSystemConfiguratorImpl implements BookSystemConfigurator {
    Logger logger = LoggerFactory.getLogger(BookingSystemConfiguratorImpl.class);

    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    TimeSlotRepository timeSlotRepository;
    @Autowired
    MeetingRoomRepository meetingRoomRepository;
    @Autowired
    BookedTimeslotRepository bookedTimeslotRepository;

    @Override
    public void initializeData() {
        if (!timeSlotRepository.findAll().iterator().hasNext()) {
            setupTimeSlots();
            setupMaintenanceTimings();
            setupMeetingRooms();
        }
    }

    private void setupTimeSlots() {
        //set up time slots from 9:00 to 18:00
        int startHour = 0;
        int endHour = 24;
        int minuteInterval;
        for (int hour = startHour; hour < endHour; hour++) {
            minuteInterval = 0;
            while (minuteInterval < 60) {
                TimeSlot timeSlot1 = new TimeSlot();
                //timeSlot1.setStartTime(LocalTime.now().withHour(hour).withMinute(minuteInterval).withSecond(0).withNano(0));
                timeSlot1.setStartTime(DateUtils.get24HourTime(hour, minuteInterval));
                minuteInterval += 15;
                if (minuteInterval == 60) {
                    //minuteInterval = 0;
                    if (hour < 23) {
                        //timeSlot1.setEndTime(LocalTime.now().withHour(hour + 1).withMinute(0).withSecond(0).withNano(0));
                        timeSlot1.setEndTime(DateUtils.get24HourTime(hour+1, 0));
                    } else {
                        //timeSlot1.setEndTime(LocalTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0));
                        timeSlot1.setEndTime(DateUtils.get24HourTime(0, 0));
                    }
                } else {
                    timeSlot1.setEndTime(LocalTime.now().withHour(hour).withMinute(minuteInterval).withSecond(0).withNano(0));
                    timeSlot1.setEndTime(DateUtils.get24HourTime(hour, minuteInterval));
                }
                timeSlot1.setMaintenance(Boolean.FALSE);
                TimeSlot saved = timeSlotRepository.save(timeSlot1);
                //logger.atInfo().log("Adding time slot ... " + saved);
            }
        }
    }

    private void setupMeetingRooms() {
        MeetingRoom meetingRoom3 = new MeetingRoom();
        meetingRoom3.setRoomName("Strive");
        meetingRoom3.setCapacity(20);
        meetingRoomRepository.save(meetingRoom3);
        logger.atInfo().log("Adding Strive Meeting room " + meetingRoom3);

        MeetingRoom meetingRoom1 = new MeetingRoom();
        meetingRoom1.setRoomName("Beauty");
        meetingRoom1.setCapacity(7);
        meetingRoomRepository.save(meetingRoom1);
        logger.atInfo().log("Adding Beauty Meeting room " + meetingRoom1);

        MeetingRoom meetingRoom2 = new MeetingRoom();
        meetingRoom2.setRoomName("Inspire");
        meetingRoom2.setCapacity(12);
        meetingRoomRepository.save(meetingRoom2);
        logger.atInfo().log("Adding Inspire Meeting room " + meetingRoom2);

        MeetingRoom meetingRoom = new MeetingRoom();
        meetingRoom.setRoomName("Amaze");
        meetingRoom.setCapacity(3);
        meetingRoomRepository.save(meetingRoom);
        logger.atInfo().log("Adding Amaze Meeting room ...." + meetingRoom);

        logger.atInfo().log("Verifying initialised meeting rooms: ");
        for (MeetingRoom room : meetingRoomRepository.findAll()) {
            logger.atInfo().log("-> " + room);
        }
    }

    private void setupMaintenanceTimings() {
        logger.atInfo().log("Adding maintenance interval for 09:00 ");
        TimeSlot maintenance1 = timeSlotRepository.findSlotTimeSlotByStartTime(LocalTime.now().withHour(9).withMinute(0).withSecond(0).withNano(0));
        maintenance1.setMaintenance(Boolean.TRUE);
        timeSlotRepository.save(maintenance1);

        logger.atInfo().log("Adding maintenance interval for 13:00 ... ");
        TimeSlot maintenance2 = timeSlotRepository.findSlotTimeSlotByStartTime(LocalTime.now().withHour(13).withMinute(0).withSecond(0).withNano(0));
        maintenance2.setMaintenance(Boolean.TRUE);
        timeSlotRepository.save(maintenance2);

        logger.atInfo().log("Adding maintenance interval for 17:00 ");
        TimeSlot maintenance3 = timeSlotRepository.findSlotTimeSlotByStartTime(LocalTime.now().withHour(17).withMinute(0).withSecond(0).withNano(0));
        maintenance3.setMaintenance(Boolean.TRUE);
        timeSlotRepository.save(maintenance3);

        logger.atInfo().log("Verifying initialised timeslots with maintenance timings: ");
        for (TimeSlot timeSlot : timeSlotRepository.findAll()) {
            logger.atInfo().log("-> " + timeSlot);
        }
    }
}
