package com.example.communalpayments.paymenthandler;

import com.example.communalpayments.entities.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Getter
@Setter
@NoArgsConstructor
@Component
public class HandledPaymentDto {

    @JsonProperty
    private long id;

    @JsonProperty
    private PaymentStatus status;
}
