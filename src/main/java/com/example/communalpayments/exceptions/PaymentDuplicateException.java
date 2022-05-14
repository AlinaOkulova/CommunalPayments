package com.example.communalpayments.exceptions;

public class PaymentDuplicateException extends Exception {

    public PaymentDuplicateException() {
        super("Такая оплата уже существует. Повторите действие через минуту");
    }
}
