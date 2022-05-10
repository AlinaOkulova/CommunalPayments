package com.example.communalpayments.web.utils;

import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.services.PaymentServiceImpl;
import com.example.communalpayments.web.dto.HandledPaymentDto;
import com.example.communalpayments.web.exceptions.PaymentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HandledPaymentMapping implements Mapping<HandledPaymentDto, Payment> {

    private final PaymentServiceImpl paymentService;

    @Autowired
    public HandledPaymentMapping(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public Payment convertDtoTo(HandledPaymentDto paymentDto) throws PaymentNotFoundException {
        Payment payment = paymentService.get(paymentDto.getId());
        payment.setStatus(paymentDto.getStatus());
        payment.setTimeStatusChange(LocalDateTime.now());
        return payment;
    }
}
