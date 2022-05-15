package com.example.communalpayments.web;

import com.example.communalpayments.entities.BillingAddress;
import com.example.communalpayments.exceptions.AddressNotFoundException;
import com.example.communalpayments.exceptions.UserNotFoundException;
import com.example.communalpayments.services.interfaces.BillingAddressService;
import com.example.communalpayments.web.dto.BillingAddressDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/billing-addresses")
public class BillingAddressController {

    private final BillingAddressService billingAddressService;

    @Autowired
    public BillingAddressController(BillingAddressService billingAddressService) {
        this.billingAddressService = billingAddressService;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getAllAddressesByUserId(@PathVariable long id) {
        try {
            List<BillingAddress> billingAddresses = billingAddressService.getAllAddressByUserId(id);
            return ResponseEntity.ok(billingAddresses);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<String> createBillingAddress(@Valid @RequestBody BillingAddressDto addressDto) {
        try {
            BillingAddress billingAddress = billingAddressService.createBillingAddress(addressDto);
            return new ResponseEntity<>("id : " + billingAddress.getId(), HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBillingAddressById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(billingAddressService.get(id));
        } catch (AddressNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
