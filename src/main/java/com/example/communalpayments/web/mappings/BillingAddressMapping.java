package com.example.communalpayments.web.mappings;

import com.example.communalpayments.entities.BillingAddress;
import com.example.communalpayments.entities.User;
import com.example.communalpayments.exceptions.UserNotFoundException;
import com.example.communalpayments.services.UserServiceImpl;
import com.example.communalpayments.web.dto.BillingAddressDto;
import org.springframework.stereotype.Service;

@Service
public class BillingAddressMapping implements Mapping<BillingAddressDto, BillingAddress> {

    private final UserServiceImpl userService;

    public BillingAddressMapping(UserServiceImpl userService) {
        this.userService = userService;
    }

    public BillingAddress convertDtoTo(BillingAddressDto billingAddressDto) throws UserNotFoundException {
        User user = userService.get(billingAddressDto.getUserId());
        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setAddress(billingAddressDto.getAddress());
        billingAddress.setUser(user);
        return billingAddress;
    }
}
