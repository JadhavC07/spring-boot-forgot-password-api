package com.example.login.signin.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = "An error occurred";

        Throwable rootCause = ex.getRootCause();
        if (rootCause instanceof SQLIntegrityConstraintViolationException) {
            String rootMessage = rootCause.getMessage();
            if (rootMessage.contains("username")) {
                message = "Username is already taken";
            } else if (rootMessage.contains("email")) {
                message = "Email is already registered";
            }
        }

        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OTPExpiredException.class)
    public ResponseEntity<String> handleOTPExpiredException(OTPExpiredException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP has expired");
    }
}
