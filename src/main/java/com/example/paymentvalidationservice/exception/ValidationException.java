package com.example.paymentvalidationservice.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

    private final String errorCode;

    public ValidationException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
