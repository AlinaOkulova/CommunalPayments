package com.example.communalpayments.services.interfaces;

import com.example.communalpayments.entities.Payment;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PaymentService {

    List<Payment> getAllPaymentsByUserId(Long userId);

    void saveAll(List<Payment> payments);

    CompletableFuture<Payment> createPayment(Payment payment);
}
