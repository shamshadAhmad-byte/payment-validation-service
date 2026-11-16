package com.example.paymentvalidationservice.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.paymentvalidationservice.dto.PaymentRequest;
import com.example.paymentvalidationservice.service.interfaces.Payment;

@Service
public class PaymentService implements Payment {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    public PaymentService() {
        log.info("PaymentService initialized");

    }

    @Override
    public String createPayment(PaymentRequest paymentRequest) {
        log.info("Processing payment");
        if (paymentRequest == null) {
            throw new IllegalArgumentException("Payment request must not be null");
        }
        log.info("Payment ID: {}", paymentRequest);
        // check 1
        // check 2
        
        return "Payment processed successfully";
    }
}
