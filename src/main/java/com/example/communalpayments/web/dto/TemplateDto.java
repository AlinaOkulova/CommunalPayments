package com.example.communalpayments.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class TemplateDto {

    @JsonProperty
    private long id;

    @JsonProperty
    private String name;

    @JsonProperty
    private String iban;

    @JsonProperty
    private String purposeOfPayment;

    @JsonProperty
    private long addressId;
}
