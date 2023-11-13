package com.udsu.auth.exception;

public class IncorrectJwtTokenException extends RuntimeException {
    public IncorrectJwtTokenException(String message) {
        super(message);
    }
}
