package com.example.communalpayments.web.mappings;

import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.entities.Template;
import com.example.communalpayments.exceptions.TemplateNotFoundException;
import com.example.communalpayments.services.interfaces.TemplateService;
import com.example.communalpayments.web.dto.PaymentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentMapping implements Mapping<PaymentDto, Payment> {

    private final TemplateService templateService;

    @Autowired
    public PaymentMapping(TemplateService templateService) {
        this.templateService = templateService;
    }

    @Override
    public Payment convertDto(PaymentDto paymentDto) throws TemplateNotFoundException {
        Template template = templateService.get(paymentDto.getTemplateId());
        String cardNumber = paymentDto.getCardNumber();
        double amount = paymentDto.getAmount();
        return new Payment(template, cardNumber, amount);
    }
}
