package com.example.communalpayments.web;

import com.example.communalpayments.entities.Template;
import com.example.communalpayments.exceptions.AddressNotFoundException;
import com.example.communalpayments.exceptions.TemplateNotFoundException;
import com.example.communalpayments.services.interfaces.TemplateService;
import com.example.communalpayments.web.dto.TemplateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    private final TemplateService templateService;

    @Autowired
    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping("/billing-address/{id}")
    public ResponseEntity<List<Template>> getAllTemplatesByAddressId(@PathVariable long id)
            throws AddressNotFoundException {

        List<Template> templates = templateService.getAllTemplatesByAddressId(id);
        return ResponseEntity.ok(templates);
    }

    @PostMapping
    public ResponseEntity<String> createTemplate(@Valid @RequestBody TemplateDto templateDto)
            throws AddressNotFoundException {

        Template template = templateService.createTemplate(templateDto);
        return new ResponseEntity<>("{\n\"id\" : " + template.getId() + "\n}", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Template> getTemplateById(@PathVariable long id) throws TemplateNotFoundException {

        return ResponseEntity.ok(templateService.get(id));
    }
}
