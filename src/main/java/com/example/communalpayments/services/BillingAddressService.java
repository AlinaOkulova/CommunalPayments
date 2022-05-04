package com.example.communalpayments.services;

import com.example.communalpayments.entities.BillingAddress;

import java.util.List;

public interface BillingAddressService {

    List<BillingAddress> getAllAddressById(Long addressId);

    BillingAddress create(BillingAddress billingAddress);

    long getAddressId(BillingAddress billingAddress);
}
