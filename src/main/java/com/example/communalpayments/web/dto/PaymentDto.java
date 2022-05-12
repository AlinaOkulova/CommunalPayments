package com.example.communalpayments.web.dto;

import com.example.communalpayments.web.validation.CheckCardNumber;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

@Data
@Component
public class PaymentDto {

    @JsonProperty
    @Min(value = 1, message = "Id должен быть больше нуля")
    private long templateId;

    @JsonProperty
    @CheckCardNumber
    private String cardNumber;

    @JsonProperty
    @DecimalMin(value = "0.01", message = "Сумма должна быть больше 0.00")
    private double amount;
}
