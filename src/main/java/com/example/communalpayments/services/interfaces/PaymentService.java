package com.example.communalpayments.services.interfaces;

import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.exceptions.PaymentDuplicateException;
import com.example.communalpayments.exceptions.TemplateNotFoundException;
import com.example.communalpayments.exceptions.UserNotFoundException;
import com.example.communalpayments.web.dto.PaymentDto;

import java.util.List;

public interface PaymentService {

    Payment createPayment(PaymentDto paymentDto) throws PaymentDuplicateException, TemplateNotFoundException;

    List<Payment> getAllPaymentsByUserId(Long userId) throws UserNotFoundException;

    void checkDuplicates(long templateId, double amount) throws PaymentDuplicateException;
}
