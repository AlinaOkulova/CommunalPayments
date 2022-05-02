package com.example.communalpayments.controllers;

import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.entities.User;
import com.example.communalpayments.services.PaymentService;
import com.example.communalpayments.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;

    @Autowired
    public PaymentController(PaymentService paymentService, UserService userService) {
        this.paymentService = paymentService;
        this.userService = userService;
    }

    @GetMapping
    public List<Payment> getAllPaymentsByUser(@RequestBody User user) {
        long userId = userService.getUserId(user);
        return paymentService.getAllById(userId);
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        Payment payment1 = paymentService.createPayment(payment);
        paymentService.save(payment1);

        return ResponseEntity.ok(payment1);
    }
}
