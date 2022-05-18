package com.example.communalpayments.web;

import com.example.communalpayments.entities.BillingAddress;
import com.example.communalpayments.exceptions.AddressNotFoundException;
import com.example.communalpayments.exceptions.UserNotFoundException;
import com.example.communalpayments.services.interfaces.BillingAddressService;
import com.example.communalpayments.web.dto.BillingAddressDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/billing-addresses")
public class BillingAddressController {

    private final BillingAddressService billingAddressService;

    @Autowired
    public BillingAddressController(BillingAddressService billingAddressService) {
        this.billingAddressService = billingAddressService;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<BillingAddress>> getAllAddressesByUserId(@PathVariable long id)
            throws UserNotFoundException {

        List<BillingAddress> billingAddresses = billingAddressService.getAllAddressByUserId(id);
        return ResponseEntity.ok(billingAddresses);
    }

    @PostMapping
    public ResponseEntity<String> createBillingAddress(@Valid @RequestBody BillingAddressDto addressDto)
            throws UserNotFoundException {

        BillingAddress billingAddress = billingAddressService.createBillingAddress(addressDto);
        return new ResponseEntity<>("{\n\"id\" : " + billingAddress.getId() + "\n}", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillingAddress> getBillingAddressById(@PathVariable Long id)
            throws AddressNotFoundException {

        return ResponseEntity.ok(billingAddressService.get(id));
    }
}
