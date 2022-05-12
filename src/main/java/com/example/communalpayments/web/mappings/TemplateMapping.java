package com.example.communalpayments.web.mappings;

import com.example.communalpayments.entities.BillingAddress;
import com.example.communalpayments.entities.Template;
import com.example.communalpayments.exceptions.AddressNotFoundException;
import com.example.communalpayments.services.BillingAddressServiceImpl;
import com.example.communalpayments.web.dto.TemplateDto;
import org.springframework.stereotype.Service;

@Service
public class TemplateMapping implements Mapping<TemplateDto, Template> {

    private final BillingAddressServiceImpl billingAddressService;

    public TemplateMapping(BillingAddressServiceImpl billingAddressService) {
        this.billingAddressService = billingAddressService;
    }

    @Override
    public Template convertDtoTo(TemplateDto templateDto) throws AddressNotFoundException {
        BillingAddress billingAddress = billingAddressService.get(templateDto.getAddressId());
        Template template = new Template();
        template.setName(templateDto.getName());
        template.setIban(templateDto.getIban());
        template.setPurposeOfPayment(templateDto.getPurposeOfPayment());
        template.setAddress(billingAddress);
        return template;
    }
}
