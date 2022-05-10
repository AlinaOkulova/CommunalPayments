package com.example.communalpayments.services.interfaces;

import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.web.exceptions.UserNotFoundException;

import java.util.List;

public interface PaymentService {

    List<Payment> getAllPaymentsByUserId(Long userId) throws UserNotFoundException;
}
