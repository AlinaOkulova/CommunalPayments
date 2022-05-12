package com.example.communalpayments.web;

import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.exceptions.PaymentDuplicateException;
import com.example.communalpayments.exceptions.PaymentNotFoundException;
import com.example.communalpayments.exceptions.TemplateNotFoundException;
import com.example.communalpayments.exceptions.UserNotFoundException;
import com.example.communalpayments.services.PaymentServiceImpl;
import com.example.communalpayments.web.dto.PaymentDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentServiceImpl paymentService;

    @Autowired
    public PaymentController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getAllPaymentsByUserId(@PathVariable long id) {
        try {
            List<Payment> payments = paymentService.getAllPaymentsByUserId(id);
            return ResponseEntity.ok(payments);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<String> createPayment(@Valid @RequestBody PaymentDto paymentDto) {
        try {
            Payment payment = paymentService.createPayment(paymentDto);
            return new ResponseEntity<>("id : " + payment.getId(), HttpStatus.CREATED);
        } catch (TemplateNotFoundException | PaymentDuplicateException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPayment(@PathVariable long id) {
        try {
            return ResponseEntity.ok(paymentService.get(id));
        } catch (PaymentNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
