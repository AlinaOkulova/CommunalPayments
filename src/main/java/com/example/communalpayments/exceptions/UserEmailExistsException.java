package com.example.communalpayments.exceptions;

public class UserEmailExistsException extends Exception {

    public UserEmailExistsException(String message) {
        super(message);
    }
}
