package com.appservices.booking.data.repository;

import com.appservices.booking.data.entity.BookedTimeslot;
import com.appservices.booking.data.entity.MeetingRoom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookedTimeslotRepository extends CrudRepository<BookedTimeslot, Long> {
    @Query("SELECT bt from BookedTimeslot bt")
    List<BookedTimeslot> findAllBookedTimeslots();

    @Query("SELECT bt.meetingRoom from BookedTimeslot bt")
    List<MeetingRoom> findAllBookedMeetingRooms();
    @Query("SELECT bt from BookedTimeslot bt where bt.meetingRoom.id = ?1")
    List<BookedTimeslot> findAllBookedMeetingRooms(Long meetingRoomId);
}
