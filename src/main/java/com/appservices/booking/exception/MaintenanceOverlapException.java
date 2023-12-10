package com.appservices.booking.exception;

public class MaintenanceOverlapException extends RuntimeException{
    public MaintenanceOverlapException() {
        super();
    }

    public MaintenanceOverlapException(String message) {
        super(message);
    }
}
