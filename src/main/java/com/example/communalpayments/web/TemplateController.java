package com.example.communalpayments.web;

import com.example.communalpayments.entities.BillingAddress;
import com.example.communalpayments.entities.Template;
import com.example.communalpayments.services.BillingAddressServiceImpl;
import com.example.communalpayments.services.TemplateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    private final BillingAddressServiceImpl billingAddressService;
    private final TemplateServiceImpl templateService;

    @Autowired
    public TemplateController(BillingAddressServiceImpl billingAddressService, TemplateServiceImpl templateService) {
        this.billingAddressService = billingAddressService;
        this.templateService = templateService;
    }

    @GetMapping
    public List<Template> getAllTemplatesByAddress(@RequestBody BillingAddress billingAddress) {
        long addressId = billingAddressService.getAddressId(billingAddress);
        return templateService.getAllTemplatesByAddressId(addressId);
    }

    @PostMapping
    public ResponseEntity<Template> createTemplate(@RequestBody Template template) {
        Template template1 = templateService.create(template);

        return ResponseEntity.ok(template1);
    }


    @GetMapping("/{id}")
    public Template getTemplateById(@PathVariable long id) {
        return templateService.get(id);
    }
}
