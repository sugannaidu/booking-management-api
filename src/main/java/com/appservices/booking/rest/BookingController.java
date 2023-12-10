package com.appservices.booking.rest;

import com.appservices.booking.data.dto.BookingDTO;
import com.appservices.booking.data.dto.MeetingRoomDTO;
import com.appservices.booking.exception.*;
import com.appservices.booking.service.BookSystemConfigurator;
import com.appservices.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Tag(name = "Booking", description = "Bookings management APIs")
@RestController
@RequestMapping("/api")
public class BookingController {
    Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    BookSystemConfigurator configurator;

    @Autowired
    BookingService service;

    @Operation(summary = "Ping booking endpoint.")
    @GetMapping("/ping")
    public String ping() {
        return "Successful ping!";
    }

    @Operation(summary = "Booking system initialization endpoint.")
    @PostMapping("/initialize")
    public ResponseEntity<String> initialize() {
        configurator.initializeData();
        return ResponseEntity.of(Optional.of("Successful initialize!"));

    }

    @Operation(summary = "Meeting room booking request.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = MeetingRoomDTO.class), mediaType = "application/json")}),
            @ApiResponse(description = "Booking error.", responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(description = "Available rooms not found.", responseCode = "417", content = {@Content(schema = @Schema())}),
            @ApiResponse(description = "Available rooms not found, maximum number of people exceeded.", responseCode = "405", content = {@Content(schema = @Schema())}),
            @ApiResponse(description = "Available rooms not found, timeslot overlaps with maintenance timeslot.", responseCode = "412", content = {@Content(schema = @Schema())}),
            @ApiResponse(description = "Minimum number of people for booking is 2.", responseCode = "406", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @PostMapping("/meeting-room/booking")
    public ResponseEntity<BookingDTO> bookMeetingRoom(String startTime, String endTime, Integer noOfPeople) {
        BookingDTO bookingDTO;
        try {
            bookingDTO = service.bookConferenceRoom(startTime, endTime, noOfPeople);
        } catch (BookingException e) {
            logger.atWarn().log(e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (RoomCapacityException e) {
            logger.atWarn().log(e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, e.getMessage(), e);
        } catch (RoomAvailabilityException e) {
            logger.atWarn().log(e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
        } catch (RoomMaxAvailabilityException e) {
            logger.atWarn().log(e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.METHOD_NOT_ALLOWED, e.getMessage(), e);
        } catch (MaintenanceOverlapException e) {
            logger.atWarn().log(e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.PRECONDITION_FAILED, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
        return new ResponseEntity<>(bookingDTO, HttpStatus.OK);
    }

    @Operation(summary = "Available meeting room request.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = MeetingRoomDTO.class), mediaType = "application/json")}),
            @ApiResponse(description = "Booking availability check error.", responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(description = "Available rooms not found, timeslot overlaps with maintenance timeslot.", responseCode = "412", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @GetMapping("/meeting-room/available")
    public ResponseEntity<List<MeetingRoomDTO>> retrieveAvailableMeetingRoom(String startTime, String endTime) {
        List<MeetingRoomDTO> availableRooms;
        try {
            availableRooms = service.checkAvailableRooms(startTime, endTime);
        } catch (BookingException e) {
            logger.atWarn().log(e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (MaintenanceOverlapException e) {
            logger.atWarn().log(e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.PRECONDITION_FAILED, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
        return new ResponseEntity<>(availableRooms, HttpStatus.OK);

    }
}
