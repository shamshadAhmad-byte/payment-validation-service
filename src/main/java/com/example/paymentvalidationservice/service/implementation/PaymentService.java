package com.example.paymentvalidationservice.service.implementation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.example.paymentvalidationservice.dto.PaymentRequest;
import com.example.paymentvalidationservice.service.implementation.validator.ValidateCheck1;
import com.example.paymentvalidationservice.service.implementation.validator.ValidateCheck2;
import com.example.paymentvalidationservice.service.interfaces.Payment;
import com.example.paymentvalidationservice.service.interfaces.Validator;

@Service
public class PaymentService implements Payment, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public String createPayment(PaymentRequest paymentRequest) {
        log.info("Processing payment");
        if (paymentRequest == null) {
            throw new IllegalArgumentException("Payment request must not be null");
        }
        log.info("Payment ID: {}", paymentRequest);
        if (applicationContext == null) {
            throw new IllegalStateException("Application context is not initialized");
        }
        List<Class<? extends Validator>> validatorTypes = List.of(
                ValidateCheck1.class,
                ValidateCheck2.class
        );
        validatorTypes.forEach(validatorType ->
                applicationContext.getBean(validatorType).validate(paymentRequest));

        return "Payment processed successfully";
    }
}
