package com.example.communalpayments.paymenthandler;

import com.example.communalpayments.dao.PaymentRepository;
import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.exceptions.PaymentNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class HandledPaymentServiceImpl implements HandledPaymentService {

    private final PaymentRepository repository;
    private final HandledPaymentMapping mapping;
    private final ConcurrentLinkedQueue<Payment> handledPayments;

    @Autowired
    public HandledPaymentServiceImpl(PaymentRepository repository, HandledPaymentMapping mapping) {
        this.repository = repository;
        this.mapping = mapping;
        this.handledPayments = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void addToSaveQueue(HandledPaymentDto paymentDto) throws PaymentNotFoundException {
        handledPayments.add(mapping.convertDto(paymentDto));
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    private void saveHandledPayments() {
        List<Payment> paymentsToSave = new ArrayList<>();
        while (!handledPayments.isEmpty()) {
            paymentsToSave.add(handledPayments.poll());
        }
        if (!paymentsToSave.isEmpty()) {
            repository.saveAll(paymentsToSave);
            log.info("Сохранил оплаты: " + paymentsToSave);
        }
    }
}
