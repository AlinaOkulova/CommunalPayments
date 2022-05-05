package com.example.communalpayments.services;

import com.example.communalpayments.dao.TemplateRepository;
import com.example.communalpayments.entities.BillingAddress;
import com.example.communalpayments.entities.Template;
import com.example.communalpayments.services.interfaces.Service;
import com.example.communalpayments.services.interfaces.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class TemplateServiceImpl implements Service<Template, Long>, TemplateService {

    private final TemplateRepository templateRepository;
    private final BillingAddressServiceImpl billingAddressService;

    @Autowired
    public TemplateServiceImpl(TemplateRepository templateRepository, BillingAddressServiceImpl billingAddressService) {
        this.templateRepository = templateRepository;
        this.billingAddressService = billingAddressService;
    }

    @Override
    public List<Template> getAllTemplatesByAddressId(Long addressId) {
        return templateRepository.getTemplatesByAddressId(addressId);
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

    @Override
    public Template create(Template template) {
        long addressId = template.getAddress().getId();
        BillingAddress billingAddress = billingAddressService.get(addressId);
        template.setAddress(billingAddress);
        save(template);

        return template;
    }
}
