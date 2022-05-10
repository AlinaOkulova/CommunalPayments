package com.example.communalpayments.services;

import com.example.communalpayments.dao.BillingAddressRepository;
import com.example.communalpayments.entities.BillingAddress;
import com.example.communalpayments.services.interfaces.BillingAddressService;
import com.example.communalpayments.services.interfaces.Service;
import com.example.communalpayments.web.exceptions.AddressNotFoundException;
import com.example.communalpayments.web.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class BillingAddressServiceImpl implements Service<BillingAddress, Long>, BillingAddressService {

    private final BillingAddressRepository billingAddressRepository;
    private final UserServiceImpl userService;

    @Autowired
    public BillingAddressServiceImpl(BillingAddressRepository billingAddressRepository, UserServiceImpl userService) {
        this.billingAddressRepository = billingAddressRepository;
        this.userService = userService;
    }

    @Override
    public List<BillingAddress> getAllAddressByUserId(Long userId) throws UserNotFoundException {
        userService.checkUserById(userId);
        return billingAddressRepository.getBillingAddressesByUserId(userId);
    }

    @Override
    public void save(BillingAddress billingAddress) {
        billingAddressRepository.save(billingAddress);
    }

    @Override
    public BillingAddress get(Long addressId) throws AddressNotFoundException {
        Optional<BillingAddress> optional = billingAddressRepository.findById(addressId);
        if (optional.isPresent()) {
            return optional.get();
        } else throw new AddressNotFoundException("Платежный адрес с заданным id не существует");
    }

    @Override
    public void checkAddressById(long addressId) throws AddressNotFoundException {
        Optional<BillingAddress> optional = billingAddressRepository.findById(addressId);
        if (optional.isEmpty()) throw new AddressNotFoundException("Платежный адрес с заданным id не существует");
    }
}
