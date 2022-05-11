package com.example.communalpayments.web;

import com.example.communalpayments.entities.Template;
import com.example.communalpayments.services.TemplateServiceImpl;
import com.example.communalpayments.web.dto.TemplateDto;
import com.example.communalpayments.exceptions.AddressNotFoundException;
import com.example.communalpayments.exceptions.TemplateNotFoundException;
import com.example.communalpayments.web.utils.TemplateMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    private final TemplateServiceImpl templateService;
    private final TemplateMapping mapping;

    @Autowired
    public TemplateController(TemplateServiceImpl templateService, TemplateMapping mapping) {
        this.templateService = templateService;
        this.mapping = mapping;
    }

    @GetMapping("/billing-address/{id}")
    public ResponseEntity<?> getAllTemplatesByAddress(@PathVariable long id) {
        try {
            List<Template> templates = templateService.getAllTemplatesByAddressId(id);
            return ResponseEntity.ok(templates);
        } catch (AddressNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<String> createTemplate(@RequestBody TemplateDto templateDto) {
        try {
            Template template = mapping.convertDtoTo(templateDto);
            templateService.save(template);
            return new ResponseEntity<>("id : " + template.getId(), HttpStatus.CREATED);
        } catch (AddressNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getTemplateById(@PathVariable long id) {
        try {
            return ResponseEntity.ok(templateService.get(id));
        } catch (TemplateNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
