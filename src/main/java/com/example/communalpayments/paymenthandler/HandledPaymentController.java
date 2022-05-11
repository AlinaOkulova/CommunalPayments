package com.example.communalpayments.paymenthandler;

import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.services.PaymentServiceImpl;
import com.example.communalpayments.exceptions.PaymentNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/handled-payments")
public class HandledPaymentController {

    private final PaymentServiceImpl paymentService;
    private final HandledPaymentMapping mapping;

    @Autowired
    public HandledPaymentController(PaymentServiceImpl paymentService, HandledPaymentMapping mapping) {
        this.paymentService = paymentService;
        this.mapping = mapping;
    }

    @PostMapping("/saving")
    public ResponseEntity<HttpStatus> savePayment(@RequestBody HandledPaymentDto paymentDto) {
        try {
            Payment payment = mapping.convertDtoTo(paymentDto);
            paymentService.save(payment);
            log.info("Обновил оплату id: " + payment.getId() + ", статус: " + payment.getStatus());
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (PaymentNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
