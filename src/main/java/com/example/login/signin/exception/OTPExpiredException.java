package com.example.login.signin.exception;

public class OTPExpiredException  extends RuntimeException{
    public OTPExpiredException(String message) {
        super(message);
    }
}
