package com.example.communalpayments.web;

import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.exceptions.PaymentDuplicateException;
import com.example.communalpayments.exceptions.PaymentNotFoundException;
import com.example.communalpayments.exceptions.TemplateNotFoundException;
import com.example.communalpayments.exceptions.UserNotFoundException;
import com.example.communalpayments.services.interfaces.PaymentService;
import com.example.communalpayments.web.dto.PaymentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Payment>> getAllPaymentsByUserId(@PathVariable long id)
            throws UserNotFoundException {

        List<Payment> payments = paymentService.getAllPaymentsByUserId(id);
        return ResponseEntity.ok(payments);
    }

    @PostMapping
    public ResponseEntity<String> createPayment(@Valid @RequestBody PaymentDto paymentDto)
            throws PaymentDuplicateException, TemplateNotFoundException {

        Payment payment = paymentService.createPayment(paymentDto);
        return new ResponseEntity<>("{\n\"id\" : " + payment.getId() + "\n}", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable long id) throws PaymentNotFoundException {

        return ResponseEntity.ok(paymentService.get(id));
    }
}
