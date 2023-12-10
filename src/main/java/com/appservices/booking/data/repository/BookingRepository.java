package com.appservices.booking.data.repository;

import com.appservices.booking.data.entity.Booking;
import com.appservices.booking.data.entity.MeetingRoom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {

  @Query("SELECT b from Booking b")
  List<Booking> findAllBookings();

}
