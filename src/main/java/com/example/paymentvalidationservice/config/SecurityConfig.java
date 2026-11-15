package com.example.paymentvalidationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import com.example.paymentvalidationservice.filter.HmacFilter;
import com.example.paymentvalidationservice.service.interfaces.HmacSHA256;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HmacSHA256 hmacSHA256, ObjectMapper objectMapper)
            throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
				.addFilterAfter(new HmacFilter(hmacSHA256, objectMapper), LogoutFilter.class);

        return http.build();
    }
}
