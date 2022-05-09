package com.example.communalpayments.web.utils;

import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.services.PaymentServiceImpl;
import com.example.communalpayments.web.dto.HandledPaymentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MappingUtils {

    private final PaymentServiceImpl paymentService;

    @Autowired
    public MappingUtils(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    public Payment convertDtoToPayment(HandledPaymentDto paymentDto) {
        Payment payment = paymentService.get(paymentDto.getId());
        payment.setStatus(paymentDto.getStatus());
        payment.setTimeStatusChange(LocalDateTime.now());
        return payment;
    }
}
