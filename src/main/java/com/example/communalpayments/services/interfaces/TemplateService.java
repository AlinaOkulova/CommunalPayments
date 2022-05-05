package com.example.communalpayments.services.interfaces;

import com.example.communalpayments.entities.Template;

import java.util.List;

public interface TemplateService {

    List<Template> getAllTemplatesByAddressId(Long addressId);

    Template create(Template template);
}
