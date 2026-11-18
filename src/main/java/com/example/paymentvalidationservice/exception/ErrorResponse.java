package com.example.paymentvalidationservice.exception;

import lombok.Data;

@Data
public class ErrorResponse {

    private final String errorCode;
    private final String errorMessage;
    private final int statusCode;
}
