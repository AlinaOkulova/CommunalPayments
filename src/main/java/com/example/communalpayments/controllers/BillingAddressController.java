package com.example.communalpayments.controllers;

import com.example.communalpayments.entities.BillingAddress;
import com.example.communalpayments.entities.User;
import com.example.communalpayments.services.BillingAddressService;
import com.example.communalpayments.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing-addresses")
public class BillingAddressController {

    private final BillingAddressService billingAddressService;
    private final UserService userService;

    @Autowired
    public BillingAddressController(BillingAddressService billingAddressService, UserService userService) {
        this.billingAddressService = billingAddressService;
        this.userService = userService;
    }

    @GetMapping
    public List<BillingAddress> getAllAddressesByUser(@RequestBody User user) {
        long userId = userService.getUserId(user);
        return billingAddressService.getAllById(userId);
    }

    @PostMapping
    public ResponseEntity<BillingAddress> createBillingAddress(@RequestBody BillingAddress billingAddress) {
        BillingAddress billingAddress1 = billingAddressService.create(billingAddress);

        return ResponseEntity.ok(billingAddress1);
    }

    @GetMapping("/{id}")
    public BillingAddress getBillingAddressById(@PathVariable Long id) {
        return billingAddressService.get(id);
    }
}
