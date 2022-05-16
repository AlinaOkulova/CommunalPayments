package com.example.communalpayments.paymenthandler;

import com.example.communalpayments.entities.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Component
public class HandledPaymentDto {

    @JsonProperty
    private long id;

    @JsonProperty
    private PaymentStatus status;
}
