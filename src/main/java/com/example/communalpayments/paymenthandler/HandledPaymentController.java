package com.example.communalpayments.paymenthandler;

import com.example.communalpayments.exceptions.PaymentNotFoundException;
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

    private final HandledPaymentService handledPaymentService;

    @Autowired
    public HandledPaymentController(HandledPaymentService handledPaymentService) {
        this.handledPaymentService = handledPaymentService;
    }

    @PostMapping("/saving")
    public ResponseEntity<HttpStatus> savePayment(@RequestBody HandledPaymentDto paymentDto)
            throws PaymentNotFoundException {

        handledPaymentService.save(paymentDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
