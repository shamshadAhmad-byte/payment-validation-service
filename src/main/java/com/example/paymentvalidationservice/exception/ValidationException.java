package com.example.paymentvalidationservice.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

    private final String errorCode;
    private final int statusCode;

    public ValidationException(String errorCode, String errorMessage, int statusCode) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
