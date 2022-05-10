package com.example.communalpayments.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class BillingAddressDto {

    @JsonProperty
    private long id;

    @JsonProperty
    private String address;

    @JsonProperty
    private long userId;
}
