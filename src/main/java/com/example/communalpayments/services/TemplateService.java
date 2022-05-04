package com.example.communalpayments.services;

import com.example.communalpayments.entities.Template;

import java.util.List;

public interface TemplateService {

    List<Template> getAllTemplatesById(Long templateId);

    Template create(Template template);
}
