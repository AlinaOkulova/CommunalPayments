package com.example.communalpayments.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Size;

@Data
@Component
public class TemplateDto {

    @JsonProperty
    private String name;

    @JsonProperty
    @Size(min = 29, max = 29, message = "iban должен содержать 29 символов")
    private String iban;

    @JsonProperty
    private String purposeOfPayment;

    @JsonProperty
    private long addressId;
}
