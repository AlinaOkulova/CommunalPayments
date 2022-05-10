package com.example.communalpayments.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class PaymentDto {

    @JsonProperty
    private long id;

    @JsonProperty
    private long templateId;

    @JsonProperty
    private String cardNumber;

    @JsonProperty
    private double amount;
}
