package com.example.communalpayments.controllers;

import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.entities.User;
import com.example.communalpayments.services.PaymentServiceImpl;
import com.example.communalpayments.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentServiceImpl paymentService;
    private final UserServiceImpl userService;

    @Autowired
    public PaymentController(PaymentServiceImpl paymentService, UserServiceImpl userService) {
        this.paymentService = paymentService;
        this.userService = userService;
    }

    @GetMapping
    public List<Payment> getAllPaymentsByUser(@RequestBody User user) {
        long userId = userService.getUserId(user);
        return paymentService.getAllByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        Payment payment1 = paymentService.createPayment(payment);
        paymentService.save(payment1);

        return ResponseEntity.ok(payment1);
    }
}
