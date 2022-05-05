package com.example.communalpayments.services.interfaces;

import com.example.communalpayments.entities.BillingAddress;

import java.util.List;

public interface BillingAddressService {

    List<BillingAddress> getAllAddressByUserId(Long userId);

    BillingAddress create(BillingAddress billingAddress);

    long getAddressId(BillingAddress billingAddress);
}
