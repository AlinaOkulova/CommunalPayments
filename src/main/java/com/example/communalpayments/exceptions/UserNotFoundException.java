package com.example.communalpayments.exceptions;

public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super("Пользователь с заданным id не существует");
    }
}
