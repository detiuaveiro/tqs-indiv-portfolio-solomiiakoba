package com.tqs.zeromonos.exception;

public class MunicipalityServiceException extends RuntimeException {
    public MunicipalityServiceException(String message) {
        super(message);
    }

    public MunicipalityServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}