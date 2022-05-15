package com.example.communalpayments.paymenthandler;

import com.example.communalpayments.dao.PaymentRepository;
import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.exceptions.PaymentNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HandledPaymentServiceImpl implements HandledPaymentService {

    private final PaymentRepository repository;
    private final HandledPaymentMapping mapping;

    @Autowired
    public HandledPaymentServiceImpl(PaymentRepository repository, HandledPaymentMapping mapping) {
        this.repository = repository;
        this.mapping = mapping;
    }

    @Override
    public void save(HandledPaymentDto paymentDto) throws PaymentNotFoundException {
        Payment payment = repository.save(mapping.convertDto(paymentDto));
        log.info("Обновил оплату id: " + payment.getId() + ", статус: " + payment.getStatus());
    }
}
