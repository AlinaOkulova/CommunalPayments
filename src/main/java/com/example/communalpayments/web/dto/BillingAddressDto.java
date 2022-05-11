package com.example.communalpayments.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Component
public class BillingAddressDto {

    @JsonProperty
    @NotNull
    @NotBlank(message = "Поле не должно быть пустым")
    private String address;

    @JsonProperty
    @Min(value = 1, message = "Id должен быть больше нуля")
    private long userId;
}
