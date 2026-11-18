package com.example.paymentvalidationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;

import com.example.paymentvalidationservice.filter.ExceptionHandlerFilter;
import com.example.paymentvalidationservice.filter.HmacFilter;
import com.example.paymentvalidationservice.service.interfaces.HmacSHA256;

import tools.jackson.databind.ObjectMapper;

@Configuration
public class SecurityConfig {

    private final HmacSHA256 hmacSHA256;
    private final ObjectMapper objectMapper;

    public SecurityConfig(HmacSHA256 hmacSHA256, ObjectMapper objectMapper) {
        this.hmacSHA256 = hmacSHA256;
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new ExceptionHandlerFilter(objectMapper), DisableEncodeUrlFilter.class)
				.addFilterAfter(new HmacFilter(hmacSHA256, objectMapper), LogoutFilter.class);

        return http.build();
    }
}
