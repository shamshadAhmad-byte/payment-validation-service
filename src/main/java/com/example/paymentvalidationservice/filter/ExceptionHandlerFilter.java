package com.example.paymentvalidationservice.filter;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import com.example.paymentvalidationservice.constant.ErrorCodeEnum;
import com.example.paymentvalidationservice.exception.ErrorResponse;
import com.example.paymentvalidationservice.exception.ValidationException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    public ExceptionHandlerFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws IOException, ServletException {
        try {
            filterChain.doFilter(request, response);
        } catch (ValidationException ex) {
            writeErrorResponse(response, new ErrorResponse(
                    ex.getErrorCode(),
                    ex.getMessage(),
                    ex.getStatusCode()), ex.getStatusCode());
        } catch (Exception ex) {
            ErrorCodeEnum error = ErrorCodeEnum.INTERNAL_SERVER_ERROR;
            writeErrorResponse(response, new ErrorResponse(
                    error.getErrorCode(),
                    error.getErrorMessage(),
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR),
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void writeErrorResponse(HttpServletResponse response, ErrorResponse errorResponse, int statusCode)
            throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        response.getWriter().flush();
    }

}
