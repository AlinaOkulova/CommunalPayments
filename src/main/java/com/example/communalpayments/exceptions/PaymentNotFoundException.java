package com.example.communalpayments.exceptions;

public class PaymentNotFoundException extends Exception {

    public PaymentNotFoundException() {
        super("Платежа с заданным id не существует");
    }
}
