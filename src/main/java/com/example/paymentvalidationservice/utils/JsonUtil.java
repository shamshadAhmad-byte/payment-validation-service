package com.example.paymentvalidationservice.utils;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import com.example.paymentvalidationservice.constant.ErrorCodeEnum;
import com.example.paymentvalidationservice.exception.ValidationException;

public final class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtil() {
    }

    public static String compactJson(Object value) {
        try {
            String json = objectMapper.writeValueAsString(value);
            JsonNode jsonNode = objectMapper.readTree(json);
            return objectMapper.writeValueAsString(jsonNode);
        } catch (Exception e) {
            ErrorCodeEnum error = ErrorCodeEnum.INVALID_JSON_FORMAT;
            throw new ValidationException(error.getErrorCode(), error.getErrorMessage(), 400);
        }
    }
}
