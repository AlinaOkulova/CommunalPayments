package com.example.communalpayments.services;

import com.example.communalpayments.dao.BillingAddressRepository;
import com.example.communalpayments.entities.BillingAddress;
import com.example.communalpayments.entities.User;
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
    public List<BillingAddress> getAllAddressById(Long addressId) {
        return billingAddressRepository.getBillingAddressesByUserId(addressId);
    }

    @Override
    public void save(BillingAddress billingAddress) {
        billingAddressRepository.save(billingAddress);
    }

    @Override
    public BillingAddress get(Long addressId) {
        BillingAddress billingAddress = null;
        Optional<BillingAddress> optional = billingAddressRepository.findById(addressId);
        if (optional.isPresent()) {
            billingAddress = optional.get();
        }
        return billingAddress;
    }

    @Override
    public BillingAddress create(BillingAddress billingAddress) {
        long userId = billingAddress.getUser().getId();
        User user = userService.get(userId);
        billingAddress.setUser(user);
        save(billingAddress);

        return billingAddress;
    }

    @Override
    public long getAddressId(BillingAddress billingAddress) {
        return billingAddress.getId();
    }
}
