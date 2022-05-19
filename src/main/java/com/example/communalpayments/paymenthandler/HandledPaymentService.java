package com.example.communalpayments.paymenthandler;

import com.example.communalpayments.exceptions.PaymentNotFoundException;

public interface HandledPaymentService {

    void addToSaveQueue(HandledPaymentDto paymentDto) throws PaymentNotFoundException;
}
