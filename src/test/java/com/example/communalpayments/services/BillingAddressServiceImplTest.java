package com.example.communalpayments.services;

import com.example.communalpayments.dao.BillingAddressRepository;
import com.example.communalpayments.dao.UserRepository;
import com.example.communalpayments.entities.BillingAddress;
import com.example.communalpayments.entities.User;
import com.example.communalpayments.exceptions.AddressNotFoundException;
import com.example.communalpayments.exceptions.UserNotFoundException;
import com.example.communalpayments.services.interfaces.BillingAddressService;
import com.example.communalpayments.web.dto.BillingAddressDto;
import com.example.communalpayments.web.mappings.BillingAddressMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BillingAddressServiceImplTest {

    private BillingAddressRepository addressRepository;
    private UserRepository userRepository;
    private BillingAddressMapping mapping;

    private BillingAddressService billingAddressService;

    private BillingAddressDto addressDto;
    private User user;
    private BillingAddress unsavedAddress;
    private BillingAddress savedAddress;


    @BeforeEach
    void setUp() {
        addressRepository = Mockito.mock(BillingAddressRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        mapping = Mockito.mock(BillingAddressMapping.class);
        billingAddressService = new BillingAddressServiceImpl(addressRepository, userRepository, mapping);

        addressDto = new BillingAddressDto("Днепр, Калиновая, 23", 2);

        user = User.builder()
                    .id(2L)
                    .lastName("Ivanov")
                    .firstName("Ivan")
                    .patronymic("Ivanovych")
                    .email("ivan@gmail.com")
                    .phoneNumber("0961254856")
                    .build();

        unsavedAddress = BillingAddress
                        .builder()
                        .id(0)
                        .address("Днепр, Калиновая, 23")
                        .user(user)
                        .build();

        savedAddress = BillingAddress
                        .builder()
                        .id(3L)
                        .address("Днепр, Калиновая, 23")
                        .user(user)
                        .build();
    }

    @Test
    void createBillingAddressTest() throws UserNotFoundException {

        when(mapping.convertDto(eq(addressDto))).thenReturn(unsavedAddress);
        when(addressRepository.save(eq(unsavedAddress))).thenReturn(savedAddress);

        BillingAddress address = billingAddressService.createBillingAddress(addressDto);

        verify(mapping, times(1)).convertDto(eq(addressDto));
        verify(addressRepository, times(1)).save(eq(unsavedAddress));

        assertEquals(3, address.getId());
        assertEquals("Днепр, Калиновая, 23", address.getAddress());
    }

    @Test
    void createBillingAddressThrowsExTest() throws UserNotFoundException {

        when(mapping.convertDto(eq(addressDto)))
                .thenThrow(new UserNotFoundException());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> billingAddressService.createBillingAddress(addressDto));

        verify(mapping, times(1)).convertDto(eq(addressDto));
        verify(addressRepository, times(0)).save(any());

        assertEquals("Пользователь с заданным id не существует", exception.getMessage());
    }

    @Test
    void getAllAddressByUserIdTest() throws UserNotFoundException {

        List<BillingAddress> usersAddresses = new ArrayList<>();
        usersAddresses.add(savedAddress);

        when(userRepository.findById(eq(2L))).thenReturn(Optional.of(user));
        when(addressRepository.getBillingAddressesByUserId(2L)).thenReturn(usersAddresses);

        List<BillingAddress> addresses = billingAddressService.getAllAddressByUserId(2L);

        verify(userRepository, times(1)).findById(eq(2L));
        verify(addressRepository, times(1)).getBillingAddressesByUserId(eq(2L));

        assertIterableEquals(usersAddresses, addresses);
    }

    @Test
    void getAllAddressByUserIdThrowsExTest() {

        when(userRepository.findById(eq(3L))).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> billingAddressService.getAllAddressByUserId(3L));

        verify(userRepository, times(1)).findById(eq(3L));
        verify(addressRepository, times(0)).getBillingAddressesByUserId(eq(3L));

        assertEquals("Пользователь с заданным id не существует", exception.getMessage());
    }

    @Test
    void getTest() throws AddressNotFoundException {

        when(addressRepository.findById(3L)).thenReturn(Optional.of(savedAddress));

        BillingAddress address = billingAddressService.get(3L);

        verify(addressRepository, times(1)).findById(eq(3L));

        assertEquals(3, address.getId());
        assertEquals("Днепр, Калиновая, 23", address.getAddress());
    }

    @Test
    void getThrowsExTest() {

        when(addressRepository.findById(3L)).thenReturn(Optional.empty());

        AddressNotFoundException exception = assertThrows(AddressNotFoundException.class,
                () -> billingAddressService.get(3L));

        verify(addressRepository, times(1)).findById(eq(3L));

        assertEquals("Платежный адрес с заданным id не существует", exception.getMessage());
    }
}