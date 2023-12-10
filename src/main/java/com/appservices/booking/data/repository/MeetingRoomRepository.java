package com.appservices.booking.data.repository;

import com.appservices.booking.data.entity.MeetingRoom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRoomRepository extends CrudRepository<MeetingRoom, Long> {
    MeetingRoom findByRoomName(String roomName);

    List<MeetingRoom> findByCapacityGreaterThanEqual(Integer capacity);

}