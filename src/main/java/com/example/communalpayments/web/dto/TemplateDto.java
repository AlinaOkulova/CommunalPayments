package com.example.communalpayments.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.*;

@Data
@Component
public class TemplateDto {

    @JsonProperty
    @NotBlank(message = "Поле не должно быть пустым")
    @NotNull
    private String name;

    @JsonProperty
    @NotNull
    @Pattern(regexp = "^UA\\d{27}$", message = "Iban должен начинаться с UA и содержать 29 символов")
    private String iban;

    @JsonProperty
    @NotBlank(message = "Поле не должно быть пустым")
    @NotNull
    private String purposeOfPayment;

    @JsonProperty
    @Min(value = 1, message = "id должен быть больше нуля")
    private long addressId;
}