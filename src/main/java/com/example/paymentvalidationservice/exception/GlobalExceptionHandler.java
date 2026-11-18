package com.example.paymentvalidationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.paymentvalidationservice.constant.ErrorCodeEnum;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException exception) {
        HttpStatus status = HttpStatus.valueOf(exception.getStatusCode());
        return ResponseEntity.status(status)
                .body(new ErrorResponse(exception.getErrorCode(), exception.getMessage(), status.value()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException exception) {
        ErrorCodeEnum error = ErrorCodeEnum.INVALID_REQUEST;
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(error.getErrorCode(), error.getErrorMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorCodeEnum error = ErrorCodeEnum.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(error.getErrorCode(), error.getErrorMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
