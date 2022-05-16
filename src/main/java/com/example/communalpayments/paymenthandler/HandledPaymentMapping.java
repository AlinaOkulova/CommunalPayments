package com.example.communalpayments.paymenthandler;

import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.exceptions.PaymentNotFoundException;
import com.example.communalpayments.services.PaymentServiceImpl;
import com.example.communalpayments.services.interfaces.PaymentService;
import com.example.communalpayments.web.mappings.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HandledPaymentMapping implements Mapping<HandledPaymentDto, Payment> {

    private final PaymentService paymentService;

    @Autowired
    public HandledPaymentMapping(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public Payment convertDto(HandledPaymentDto paymentDto) throws PaymentNotFoundException {
        Payment payment = paymentService.get(paymentDto.getId());
        payment.setStatus(paymentDto.getStatus());
        payment.setTimeStatusChange(LocalDateTime.now());
        return payment;
    }
}
