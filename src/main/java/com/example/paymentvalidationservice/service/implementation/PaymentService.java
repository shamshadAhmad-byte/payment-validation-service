package com.example.paymentvalidationservice.service.implementation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.example.paymentvalidationservice.constant.ErrorCodeEnum;
import com.example.paymentvalidationservice.constant.ValidatorRulEnum;
import com.example.paymentvalidationservice.dto.PaymentRequest;
import com.example.paymentvalidationservice.exception.ValidationException;
import com.example.paymentvalidationservice.service.interfaces.Payment;

@Service
public class PaymentService implements Payment, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private ApplicationContext applicationContext;
    @Value("${validator.rulelist}")
    private String rulelist;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public String createPayment(PaymentRequest paymentRequest) {
        log.info("Processing payment");
        if (paymentRequest == null) {
            ErrorCodeEnum error = ErrorCodeEnum.INVALID_REQUEST;
            throw new ValidationException(error.getErrorCode(), error.getErrorMessage(), 400);
        }
        log.info("Payment ID: {}", paymentRequest);
        if (applicationContext == null) {
            ErrorCodeEnum error = ErrorCodeEnum.APPLICATION_CONTEXT_NOT_INITIALIZED;
            throw new ValidationException(error.getErrorCode(), error.getErrorMessage(), 500);
        }
        List<String> validatorNames = List.of(rulelist.split(","));
        validatorNames.forEach(validatorName -> {
            ValidatorRulEnum validatorRule = ValidatorRulEnum.valueOf(validatorName.trim());
            applicationContext.getBean(validatorRule.getValidatorClass()).validate(paymentRequest);
        });

        return "Payment processed successfully";
    }
}
