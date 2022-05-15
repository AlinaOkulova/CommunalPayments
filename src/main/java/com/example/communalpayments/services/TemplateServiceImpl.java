package com.example.communalpayments.services;

import com.example.communalpayments.dao.BillingAddressRepository;
import com.example.communalpayments.dao.TemplateRepository;
import com.example.communalpayments.entities.BillingAddress;
import com.example.communalpayments.entities.Template;
import com.example.communalpayments.exceptions.AddressNotFoundException;
import com.example.communalpayments.exceptions.TemplateNotFoundException;
import com.example.communalpayments.services.interfaces.GetService;
import com.example.communalpayments.services.interfaces.TemplateService;
import com.example.communalpayments.web.dto.TemplateDto;
import com.example.communalpayments.web.mappings.TemplateMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;
    private final BillingAddressRepository billingAddressRepository;
    private final TemplateMapping mapping;

    @Autowired
    public TemplateServiceImpl(TemplateRepository templateRepository, BillingAddressRepository billingAddressRepository,
                               TemplateMapping mapping) {
        this.templateRepository = templateRepository;
        this.billingAddressRepository = billingAddressRepository;
        this.mapping = mapping;
    }

    @Override
    public Template createTemplate(TemplateDto templateDto) throws AddressNotFoundException {
        Template template = templateRepository.save(mapping.convertDto(templateDto));
        log.info("Сохранил шаблон: " + template);
        return template;
    }

    @Override
    public List<Template> getAllTemplatesByAddressId(Long addressId) throws AddressNotFoundException {
        Optional<BillingAddress> optional = billingAddressRepository.findById(addressId);
        if (optional.isEmpty()) throw new AddressNotFoundException();
        return templateRepository.getTemplatesByAddressId(addressId);
    }

    @Override
    public Template get(Long templateId) throws TemplateNotFoundException {
        Optional<Template> optional = templateRepository.findById(templateId);
        if (optional.isPresent()) {
            return optional.get();
        } else throw new TemplateNotFoundException();
    }
}
