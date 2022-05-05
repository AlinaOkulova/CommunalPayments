package com.example.communalpayments.services;

import com.example.communalpayments.entities.Payment;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PaymentService {

    List<Payment> getAllByUserId(Long userId);

    void saveAll(List<Payment> payments);

    CompletableFuture<Payment> createPayment(Payment payment);
}
