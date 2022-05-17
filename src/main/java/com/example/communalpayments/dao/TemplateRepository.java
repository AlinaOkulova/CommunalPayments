package com.example.communalpayments.dao;

import com.example.communalpayments.entities.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TemplateRepository extends JpaRepository<Template, Long> {

    List<Template> getTemplatesByAddressId(long id);

    @Modifying
    @Transactional
    @Query(value = "truncate table communal_payments.templates restart identity cascade", nativeQuery = true)
    void truncateForTest();
}
