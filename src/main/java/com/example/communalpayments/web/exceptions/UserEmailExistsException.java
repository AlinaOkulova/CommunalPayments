package com.example.communalpayments.web.exceptions;

public class UserEmailExistsException extends Exception {

    public UserEmailExistsException(String message) {
        super(message);
    }
}
