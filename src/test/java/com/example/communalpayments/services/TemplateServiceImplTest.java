package com.example.communalpayments.services;

import com.example.communalpayments.dao.BillingAddressRepository;
import com.example.communalpayments.dao.TemplateRepository;
import com.example.communalpayments.entities.BillingAddress;
import com.example.communalpayments.entities.Template;
import com.example.communalpayments.exceptions.AddressNotFoundException;
import com.example.communalpayments.exceptions.TemplateNotFoundException;
import com.example.communalpayments.services.interfaces.TemplateService;
import com.example.communalpayments.web.dto.TemplateDto;
import com.example.communalpayments.web.mappings.TemplateMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TemplateServiceImplTest {

    private TemplateRepository templateRepository;
    private BillingAddressRepository billingAddressRepository;
    private TemplateMapping mapping;

    private TemplateService templateService;

    private TemplateDto templateDto;
    private BillingAddress billingAddress;
    private Template unsavedTemplate;
    private Template savedTemplate;

    @BeforeEach
    void setUp() {
        templateRepository = Mockito.mock(TemplateRepository.class);
        billingAddressRepository = Mockito.mock(BillingAddressRepository.class);
        mapping = Mockito.mock(TemplateMapping.class);
        templateService = new TemplateServiceImpl(templateRepository, billingAddressRepository, mapping);

        templateDto = new TemplateDto("отопление", "UA231236580000000123654800023",
                "отопление лиц. счет 123658", 3);

        billingAddress = BillingAddress
                .builder()
                .id(3L)
                .address("Днепр, Калиновая, 23")
                .build();

        unsavedTemplate = Template
                .builder()
                .id(0L)
                .name("отопление")
                .iban("UA231236580000000123654800023")
                .purposeOfPayment("отопление лиц. счет 123658")
                .address(billingAddress)
                .build();

        savedTemplate = Template
                .builder()
                .id(5L)
                .name("отопление")
                .iban("UA231236580000000123654800023")
                .purposeOfPayment("отопление лиц. счет 123658")
                .address(billingAddress)
                .build();
    }

    @Test
    void createTemplateTest() throws AddressNotFoundException {

        when(mapping.convertDto(eq(templateDto))).thenReturn(unsavedTemplate);
        when(templateRepository.save(eq(unsavedTemplate))).thenReturn(savedTemplate);

        Template template = templateService.createTemplate(templateDto);

        verify(mapping, times(1)).convertDto(eq(templateDto));
        verify(templateRepository, times(1)).save(eq(unsavedTemplate));

        assertEquals(5L, template.getId());
        assertEquals("отопление", template.getName());
        assertEquals("UA231236580000000123654800023", template.getIban());
        assertEquals("отопление лиц. счет 123658", template.getPurposeOfPayment());
    }

    @Test
    void createTemplateTrowsExTest() throws AddressNotFoundException {

        when(mapping.convertDto(eq(templateDto))).thenThrow(new AddressNotFoundException());

        AddressNotFoundException exception = assertThrows(AddressNotFoundException.class,
                () -> templateService.createTemplate(templateDto));

        verify(mapping, times(1)).convertDto(eq(templateDto));
        verify(templateRepository, times(0)).save(any());

        assertEquals("Платежный адрес с заданным id не существует", exception.getMessage());
    }

    @Test
    void getAllTemplatesByAddressIdTest() throws AddressNotFoundException {

        List<Template> templates = new ArrayList<>();
        templates.add(savedTemplate);

        when(billingAddressRepository.findById(eq(3L))).thenReturn(Optional.of(billingAddress));
        when(templateRepository.getTemplatesByAddressId(eq(3L))).thenReturn(templates);

        List<Template> addressTemplates = templateService.getAllTemplatesByAddressId(3L);

        verify(billingAddressRepository, times(1)).findById(eq(3L));
        verify(templateRepository, times(1)).getTemplatesByAddressId(eq(3L));

        assertIterableEquals(templates, addressTemplates);
    }

    @Test
    void getAllTemplatesByAddressIdThrowsExTest() {

        when(billingAddressRepository.findById(eq(4L))).thenReturn(Optional.empty());

        AddressNotFoundException exception = assertThrows(AddressNotFoundException.class,
                () -> templateService.getAllTemplatesByAddressId(4L));

        verify(billingAddressRepository, times(1)).findById(eq(4L));
        verify(templateRepository, times(0)).getTemplatesByAddressId(eq(4L));

        assertEquals("Платежный адрес с заданным id не существует", exception.getMessage());
    }

    @Test
    void getTest() throws TemplateNotFoundException {

        when(templateRepository.findById(eq(5L))).thenReturn(Optional.of(savedTemplate));

        Template template = templateService.get(5L);

        verify(templateRepository, times(1)).findById(eq(5L));

        assertEquals(5L, template.getId());
        assertEquals("отопление", template.getName());
        assertEquals("UA231236580000000123654800023", template.getIban());
        assertEquals("отопление лиц. счет 123658", template.getPurposeOfPayment());
    }

    @Test
    void getThrowsExTest() {

        when(templateRepository.findById(eq(6L))).thenReturn(Optional.empty());

        TemplateNotFoundException exception = assertThrows(TemplateNotFoundException.class,
                () -> templateService.get(6L));

        verify(templateRepository, times(1)).findById(eq(6L));

        assertEquals("Шаблон с заданным id не существует", exception.getMessage());
    }
}