package com.example.communalpayments.services;

import com.example.communalpayments.dao.BillingAddressRepository;
import com.example.communalpayments.entities.BillingAddress;
import com.example.communalpayments.exceptions.AddressNotFoundException;
import com.example.communalpayments.exceptions.UserNotFoundException;
import com.example.communalpayments.services.interfaces.BillingAddressService;
import com.example.communalpayments.services.interfaces.GetService;
import com.example.communalpayments.web.dto.BillingAddressDto;
import com.example.communalpayments.web.mappings.BillingAddressMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BillingAddressServiceImpl implements GetService<BillingAddress, Long>, BillingAddressService {

    private final BillingAddressRepository billingAddressRepository;
    private final UserServiceImpl userService;
    private final BillingAddressMapping mapping;

    @Autowired
    public BillingAddressServiceImpl(BillingAddressRepository billingAddressRepository, UserServiceImpl userService,
                                     BillingAddressMapping mapping) {
        this.billingAddressRepository = billingAddressRepository;
        this.userService = userService;
        this.mapping = mapping;
    }

    @Override
    public BillingAddress createBillingAddress(BillingAddressDto addressDto) throws UserNotFoundException {
        BillingAddress billingAddress = mapping.convertDtoTo(addressDto);
        return billingAddressRepository.save(billingAddress);
    }

    @Override
    public List<BillingAddress> getAllAddressByUserId(Long userId) throws UserNotFoundException {
        userService.get(userId);
        return billingAddressRepository.getBillingAddressesByUserId(userId);
    }

    @Override
    public BillingAddress get(Long addressId) throws AddressNotFoundException {
        Optional<BillingAddress> optional = billingAddressRepository.findById(addressId);
        if (optional.isPresent()) {
            return optional.get();
        } else throw new AddressNotFoundException("Платежный адрес с заданным id не существует");
    }
}
