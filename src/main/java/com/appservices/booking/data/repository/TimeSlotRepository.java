package com.appservices.booking.data.repository;

import com.appservices.booking.data.entity.TimeSlot;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Set;
@Repository
public interface TimeSlotRepository extends CrudRepository<TimeSlot, Long> {
    TimeSlot findSlotTimeSlotByStartTime(LocalTime startTime);

    @Query("SELECT t from TimeSlot t WHERE t.startTime >= ?1 and t.endTime <= ?2 ")
    Set<TimeSlot> findTimeslotsByStartTimeAndEndTime(LocalTime startTime, LocalTime endTime);

    @Query("SELECT t from TimeSlot t WHERE t.startTime >= ?1 and t.endTime <= ?2  and t.maintenance = ?3")
    Set<TimeSlot> findTimeslotsByStartTimeAndEndTimeAndMaintenance(LocalTime startTime, LocalTime endTime, Boolean maintenance);
}
