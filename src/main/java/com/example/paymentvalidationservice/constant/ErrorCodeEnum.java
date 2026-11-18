package com.example.paymentvalidationservice.constant;

import lombok.Getter;

@Getter 
public enum ErrorCodeEnum {

    INTERNAL_SERVER_ERROR("1000", "An unexpected error occurred"),
    INVALID_REQUEST("1002", "Payment request must not be null"),
    APPLICATION_CONTEXT_NOT_INITIALIZED("1003", "Application context is not initialized"),
    INVALID_JSON_FORMAT("1004", "Invalid JSON format"),
    HMAC_GENERATION_FAILED("1005", "Unable to generate HMAC-SHA256 signature");

    private final String errorCode;
    private final String errorMessage;

    ErrorCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
