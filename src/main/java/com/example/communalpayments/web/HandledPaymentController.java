//package com.example.communalpayments.web;
//
//import com.example.communalpayments.entities.Payment;
//import com.example.communalpayments.services.PaymentServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/handled-payments")
//public class HandledPaymentController {
//
//    private final PaymentServiceImpl paymentService;
//
//    @Autowired
//    public HandledPaymentController(PaymentServiceImpl paymentService) {
//        this.paymentService = paymentService;
//    }
//
//    @PostMapping("/save")
//    public ResponseEntity<HttpStatus> savePayment(@RequestBody List<Payment> payments) {
//        paymentService.saveAll(payments);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
//}
