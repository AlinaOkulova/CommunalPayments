package com.example.communalpayments.services.interfaces;

import com.example.communalpayments.entities.Template;
import com.example.communalpayments.exceptions.AddressNotFoundException;
import com.example.communalpayments.web.dto.TemplateDto;

import java.util.List;

public interface TemplateService {

    Template createTemplate(TemplateDto templateDto) throws AddressNotFoundException;

    List<Template> getAllTemplatesByAddressId(Long addressId) throws AddressNotFoundException;
}
