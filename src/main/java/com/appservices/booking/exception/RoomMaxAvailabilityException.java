package com.appservices.booking.exception;

public class RoomMaxAvailabilityException extends RuntimeException {
    public RoomMaxAvailabilityException() {
        super();
    }

    public RoomMaxAvailabilityException(String message) {
        super(message);
    }
}
