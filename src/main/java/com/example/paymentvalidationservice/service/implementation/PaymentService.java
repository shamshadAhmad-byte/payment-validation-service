package com.example.paymentvalidationservice.service.implementation;

import java.security.GeneralSecurityException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.paymentvalidationservice.dto.PaymentRequest;
import com.example.paymentvalidationservice.service.interfaces.HmacSHA256;
import com.example.paymentvalidationservice.service.interfaces.Payment;

@Service
public class PaymentService implements Payment {

    private final HmacSHA256 hmacSHA256;

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    public PaymentService(HmacSHA256 hmacSHA256) {
        this.hmacSHA256 = hmacSHA256;
        log.info("PaymentService initialized");

    }

    @Override
    public String createPayment(PaymentRequest paymentRequest, String receivedSignature) {
        log.info("Processing payment");
        log.info("Received signature: {}", receivedSignature);
        if (paymentRequest == null) {
            throw new IllegalArgumentException("Payment request must not be null");
        }
        try {
            String generateSignature = hmacSHA256.generateHmac(paymentRequest.toString(), "your_secret_key");
            log.info("Generated signature: {}", generateSignature);
           Boolean isVerified = hmacSHA256.isVerified(generateSignature, receivedSignature);
           if (!isVerified) {
                log.warn("Payment signature verification failed");
                throw new InvalidPaymentSignatureException();
            }
            return "Payment processed successfully";
        } catch (GeneralSecurityException ex) {
            log.error("Unable to validate payment signature", ex);
            throw new IllegalStateException("Unable to validate payment signature", ex);
        }
    }
}
