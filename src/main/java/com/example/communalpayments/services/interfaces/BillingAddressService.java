package com.example.communalpayments.services.interfaces;

import com.example.communalpayments.entities.BillingAddress;
import com.example.communalpayments.exceptions.AddressNotFoundException;
import com.example.communalpayments.exceptions.UserNotFoundException;
import com.example.communalpayments.web.dto.BillingAddressDto;

import java.util.List;

public interface BillingAddressService extends GetService<BillingAddress, Long> {

    BillingAddress get(Long id) throws AddressNotFoundException;

    BillingAddress createBillingAddress(BillingAddressDto addressDto) throws UserNotFoundException;

    List<BillingAddress> getAllAddressByUserId(Long userId) throws UserNotFoundException;
}
