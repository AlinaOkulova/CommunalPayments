package com.example.communalpayments.services;

import com.example.communalpayments.dao.PaymentRepository;
import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.entities.Template;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class PaymentService implements Service<Payment, Long> {

    private final PaymentRepository paymentRepository;
    private final TemplateService templateService;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, TemplateService templateService) {
        this.paymentRepository = paymentRepository;
        this.templateService = templateService;
    }

    @Override
    public List<Payment> getAllById(Long userId) {
        return paymentRepository.getAllByUserId(userId);
    }

    @Override
    public void save(Payment payment) {
        paymentRepository.save(payment);
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

    public Payment createPayment(Payment payment) {
        long templateId = payment.getTemplate().getId();
        Template template = templateService.get(templateId);
        String cardNumber = payment.getCardNumber();
        double amount = payment.getAmount();
        return new Payment(template, cardNumber, amount);
    }
}
