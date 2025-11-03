package com.tqs.zeromonos.exception;

// em vez de sempre utilizar runtimeexception
public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String message) {
        super(message);
    }
    public BookingNotFoundException(String token, String message) {
        super(String.format("Booking with token '%s' not found: %s", token, message));
    }
}