package com.example.communalpayments.web;

import com.example.communalpayments.entities.Template;
import com.example.communalpayments.exceptions.AddressNotFoundException;
import com.example.communalpayments.exceptions.TemplateNotFoundException;
import com.example.communalpayments.services.interfaces.TemplateService;
import com.example.communalpayments.web.dto.TemplateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    private final TemplateService templateService;

    @Autowired
    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping("/billing-address/{id}")
    public ResponseEntity<?> getAllTemplatesByAddressId(@PathVariable long id) {
        try {
            List<Template> templates = templateService.getAllTemplatesByAddressId(id);
            return ResponseEntity.ok(templates);
        } catch (AddressNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<String> createTemplate(@Valid @RequestBody TemplateDto templateDto) {
        try {
            Template template = templateService.createTemplate(templateDto);
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
