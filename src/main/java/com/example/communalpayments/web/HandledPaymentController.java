package com.example.communalpayments.web;

import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.services.PaymentServiceImpl;
import com.example.communalpayments.web.dto.HandledPaymentDto;
import com.example.communalpayments.web.utils.MappingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/handled-payments")
public class HandledPaymentController {

    private final PaymentServiceImpl paymentService;
    private final MappingUtils mappingUtils;

    @Autowired
    public HandledPaymentController(PaymentServiceImpl paymentService, MappingUtils mappingUtils) {
        this.paymentService = paymentService;
        this.mappingUtils = mappingUtils;
    }

    @PostMapping("/save")
    public ResponseEntity<HttpStatus> savePayment(@RequestBody HandledPaymentDto paymentDto) {
        Payment payment = mappingUtils.convertDtoToPayment(paymentDto);
        paymentService.save(payment);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
