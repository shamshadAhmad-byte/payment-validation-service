package com.example.paymentvalidationservice.service.interfaces;

import com.example.paymentvalidationservice.dto.PaymentRequest;

public interface Validator {
    void validate(PaymentRequest paymentRequest);
}
