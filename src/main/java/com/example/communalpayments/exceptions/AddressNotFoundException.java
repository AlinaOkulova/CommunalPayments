package com.example.communalpayments.exceptions;

public class AddressNotFoundException extends Exception {

    public AddressNotFoundException() {
        super("Платежный адрес с заданным id не существует");
    }
}
