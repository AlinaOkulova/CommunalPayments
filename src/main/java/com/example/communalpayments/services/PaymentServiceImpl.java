package com.example.communalpayments.services;

import com.example.communalpayments.dao.PaymentRepository;
import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.entities.Template;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class PaymentServiceImpl implements Service<Payment, Long>, PaymentService {

    private final PaymentRepository paymentRepository;
    private final TemplateServiceImpl templateService;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, TemplateServiceImpl templateService) {
        this.paymentRepository = paymentRepository;
        this.templateService = templateService;
    }

    @Override
    public List<Payment> getAllByUserId(Long userId) {
        return paymentRepository.getAllByUserId(userId);
    }

    @Override
    public void save(Payment payment) {
        paymentRepository.save(payment);
    }

    @Override
    public void saveAll(List<Payment> payments) {
        paymentRepository.saveAll(payments);
    }

    @Override
    public Payment get(Long id) {
        Payment payment = null;
        Optional<Payment> optional = paymentRepository.findById(id);
        if(optional.isPresent()) {
            payment = optional.get();
        }
        return payment;
    }

    @Override
    public Payment createPayment(Payment payment) {
        long templateId = payment.getTemplate().getId();
        Template template = templateService.get(templateId);
        String cardNumber = payment.getCardNumber();
        double amount = payment.getAmount();
        return new Payment(template, cardNumber, amount);
    }
}
