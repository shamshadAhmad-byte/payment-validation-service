package com.example.paymentvalidationservice.service.implementation.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.paymentvalidationservice.dto.PaymentRequest;
import com.example.paymentvalidationservice.service.interfaces.Validator;

@Service("CHECK2")
public class ValidatorCheck2 implements Validator {

    private static final Logger log = LoggerFactory.getLogger(ValidatorCheck2.class);

    @Override
    public void validate(PaymentRequest paymentRequest) {
        // Implement your validation logic here
        log.info(">> ValidateCheck2: Validating payment request");

        // Add more validation checks as needed
    }

}