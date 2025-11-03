package com.tqs.zeromonos.exception;

public class ServiceLimitExceededException extends RuntimeException {
    public ServiceLimitExceededException(String message) {
        super(message);
    }
}
