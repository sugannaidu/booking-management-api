package com.appservices.booking.exception;

public class BookingException extends RuntimeException {
    public BookingException() {
    }

    public BookingException(String message) {
        super(message);
    }
}
