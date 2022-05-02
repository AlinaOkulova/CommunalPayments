package com.example.communalpayments.services;

import com.example.communalpayments.dao.TemplateRepository;
import com.example.communalpayments.entities.BillingAddress;
import com.example.communalpayments.entities.Template;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class TemplateService implements Service<Template, Long> {

    private final TemplateRepository templateRepository;
    private final BillingAddressService billingAddressService;

    @Autowired
    public TemplateService(TemplateRepository templateRepository, BillingAddressService billingAddressService) {
        this.templateRepository = templateRepository;
        this.billingAddressService = billingAddressService;
    }

    @Override
    public List<Template> getAllById(Long templateId) {
        return templateRepository.getTemplatesByAddressId(templateId);
    }

    @Override
    public void save(Template template) {
        templateRepository.save(template);
    }

    @Override
    public Template get(Long templateId) {
        Template template = null;
        Optional<Template> optional = templateRepository.findById(templateId);
        if (optional.isPresent()) {
            template = optional.get();
        }
        return template;
    }

    public Template create(Template template) {
        long addressId = template.getAddress().getId();
        BillingAddress billingAddress = billingAddressService.get(addressId);
        template.setAddress(billingAddress);
        save(template);

        return template;
    }
}
