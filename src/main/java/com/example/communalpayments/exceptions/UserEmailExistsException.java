package com.example.communalpayments.exceptions;

public class UserEmailExistsException extends Exception {

    public UserEmailExistsException() {
        super("Пользователь с заданным email уже существует");
    }
}
