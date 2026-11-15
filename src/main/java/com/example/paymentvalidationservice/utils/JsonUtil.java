package com.example.paymentvalidationservice.utils;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

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
            throw new IllegalArgumentException("Invalid JSON format", e);
        }
    }
}
