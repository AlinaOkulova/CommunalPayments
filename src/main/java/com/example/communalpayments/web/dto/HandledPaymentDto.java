package com.example.communalpayments.web.dto;

import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.entities.PaymentStatus;
import com.example.communalpayments.services.PaymentServiceImpl;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
