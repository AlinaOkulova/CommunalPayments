package com.example.communalpayments.services.interfaces;

import com.example.communalpayments.entities.BillingAddress;
import com.example.communalpayments.exceptions.AddressNotFoundException;
import com.example.communalpayments.exceptions.UserNotFoundException;

import java.util.List;

public interface BillingAddressService {

    List<BillingAddress> getAllAddressByUserId(Long userId) throws UserNotFoundException;

    void checkAddressById(long addressId) throws AddressNotFoundException;
}
