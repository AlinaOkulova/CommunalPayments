package com.example.communalpayments.services;

import com.example.communalpayments.entities.Payment;

import java.util.List;

public interface PaymentService {

    List<Payment> getAllByUserId(Long userId);

    void saveAll(List<Payment> payments);

    Payment createPayment(Payment payment);
}
