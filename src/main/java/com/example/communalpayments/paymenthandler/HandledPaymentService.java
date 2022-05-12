package com.example.communalpayments.paymenthandler;

import com.example.communalpayments.exceptions.PaymentNotFoundException;

public interface HandledPaymentService {

    void save(HandledPaymentDto paymentDto) throws PaymentNotFoundException;
}
