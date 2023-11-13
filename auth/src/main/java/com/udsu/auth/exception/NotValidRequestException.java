package com.udsu.auth.exception;

public class NotValidRequestException extends RuntimeException{
    public NotValidRequestException(String message) {
        super(message);
    }
}
