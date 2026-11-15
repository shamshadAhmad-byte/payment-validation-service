package com.example.paymentvalidationservice.service.implementation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidPaymentSignatureException extends RuntimeException {

    public InvalidPaymentSignatureException() {
        super("Invalid payment signature");
    }
}
