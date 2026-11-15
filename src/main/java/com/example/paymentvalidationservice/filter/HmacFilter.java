package com.example.paymentvalidationservice.filter;

import java.io.IOException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HmacFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(HmacFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info(">> HmacFilter: Request URI: {}", request.getRequestURI());
        // Implement HMAC validation logic here

        log.info(">> in HmacFilter ");
        boolean isVerified = true; // Replace this with actual verification logic
        String hmacSignature = request.getHeader("X-Payment-Signature");

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authentication =new UsernamePasswordAuthenticationToken(
                hmacSignature,
                null,
                Collections.emptyList()
        );
        request.setAttribute("authentication", authentication);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        if (isVerified) {
            log.info(">> HmacFilter: Signature verified, proceeding further");
            filterChain.doFilter(request, response);
        } else {
            log.warn("HMAC signature verification failed");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

}
