package com.appservices.booking.exception;

public class RoomAvailabilityException extends  RuntimeException {
    public RoomAvailabilityException() {
        super();
    }

    public RoomAvailabilityException(String message) {
        super(message);
    }
}
