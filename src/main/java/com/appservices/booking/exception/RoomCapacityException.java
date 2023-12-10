package com.appservices.booking.exception;

public class RoomCapacityException extends RuntimeException {
    public RoomCapacityException() {
        super();
    }

    public RoomCapacityException(String message) {
        super(message);
    }
}
