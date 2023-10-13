package com.udsu.matcher.exception;

public class NotMatchingUserBankAccount extends RuntimeException{
    public NotMatchingUserBankAccount(String message) {
        super(message);
    }
}
