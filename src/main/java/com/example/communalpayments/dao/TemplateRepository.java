package com.example.communalpayments.dao;

import com.example.communalpayments.entities.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplateRepository extends JpaRepository<Template, Long> {

    List<Template> getTemplatesByAddressId(long id);
}
