package com.example.communalpayments.web.utils;

import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.entities.Template;
import com.example.communalpayments.services.TemplateServiceImpl;
import com.example.communalpayments.web.dto.PaymentDto;
import com.example.communalpayments.web.exceptions.TemplateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentMapping implements Mapping<PaymentDto, Payment> {

    private final TemplateServiceImpl templateService;

    @Autowired
    public PaymentMapping(TemplateServiceImpl templateService) {
        this.templateService = templateService;
    }

    @Override
    public Payment convertDtoTo(PaymentDto paymentDto) throws TemplateNotFoundException {
        Template template = templateService.get(paymentDto.getTemplateId());
        String cardNumber = paymentDto.getCardNumber();
        double amount = paymentDto.getAmount();
        return new Payment(template, cardNumber, amount);
    }
}
