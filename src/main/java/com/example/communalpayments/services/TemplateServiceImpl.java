package com.example.communalpayments.services;

import com.example.communalpayments.dao.TemplateRepository;
import com.example.communalpayments.entities.Template;
import com.example.communalpayments.services.interfaces.Service;
import com.example.communalpayments.services.interfaces.TemplateService;
import com.example.communalpayments.exceptions.AddressNotFoundException;
import com.example.communalpayments.exceptions.TemplateNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Slf4j
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
    public List<Template> getAllTemplatesByAddressId(Long addressId) throws AddressNotFoundException {
        billingAddressService.checkAddressById(addressId);
        return templateRepository.getTemplatesByAddressId(addressId);
    }

    @Override
    public void save(Template template) {
        templateRepository.save(template);
        log.info("Сохранил шаблон: " + template);
    }

    @Override
    public Template get(Long templateId) throws TemplateNotFoundException {
        Optional<Template> optional = templateRepository.findById(templateId);
        if (optional.isPresent()) {
            return optional.get();
        } else throw new TemplateNotFoundException("Шаблон с заданным id не существует");
    }
}
