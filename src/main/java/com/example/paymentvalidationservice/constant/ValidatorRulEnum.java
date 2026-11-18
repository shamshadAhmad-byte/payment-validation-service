package com.example.paymentvalidationservice.constant;

import com.example.paymentvalidationservice.service.implementation.validator.ValidatorCheck1;
import com.example.paymentvalidationservice.service.implementation.validator.ValidatorCheck2;
import com.example.paymentvalidationservice.service.interfaces.Validator;

public enum ValidatorRulEnum {

    CHECK1("CHECK1", ValidatorCheck1.class),
    CHECK2("CHECK2", ValidatorCheck2.class);

    private final String validatorName;
    private final Class<? extends Validator> validatorClass;

    ValidatorRulEnum(String validatorName, Class<? extends Validator> validatorClass) {
        this.validatorName = validatorName;
        this.validatorClass = validatorClass;
    }

    public String getValidatorName() {
        return validatorName;
    }

    public Class<? extends Validator> getValidatorClass() {
        return validatorClass;
    }
}
