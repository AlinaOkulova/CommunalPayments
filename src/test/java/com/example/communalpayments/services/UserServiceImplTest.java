package com.example.communalpayments.services;

import com.example.communalpayments.dao.UserRepository;
import com.example.communalpayments.entities.User;
import com.example.communalpayments.exceptions.UserEmailExistsException;
import com.example.communalpayments.exceptions.UserNotFoundException;
import com.example.communalpayments.web.dto.UserDto;
import com.example.communalpayments.web.mappings.UserMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private UserMapping userMapping;

    private UserServiceImpl userService;

    private UserDto userDto;
    private User unsavedUser;
    private User savedUser;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userMapping = Mockito.mock(UserMapping.class);
        userService = new UserServiceImpl(userRepository, userMapping);

        userDto = new UserDto("Ivanov", "Ivan",
                "Ivanovych", "ivan@gmail.com", "0961254856");

        unsavedUser = User.builder()
                .id(0)
                .lastName("Ivanov")
                .firstName("Ivan")
                .patronymic("Ivanovych")
                .email("ivan@gmail.com")
                .phoneNumber("0961254856")
                .build();

        savedUser = User.builder()
                .id(2L)
                .lastName("Ivanov")
                .firstName("Ivan")
                .patronymic("Ivanovych")
                .email("ivan@gmail.com")
                .phoneNumber("0961254856")
                .build();
    }

    @Test
    void registrationTest() throws UserEmailExistsException {

        doReturn(unsavedUser).when(userMapping).convertDtoTo(eq(userDto));
        doReturn(savedUser).when(userRepository).save(eq(unsavedUser));

        User user = userService.registration(userDto);

        verify(userMapping, times(1)).convertDtoTo(eq(userDto));

        verify(userRepository, times(1)).save(eq(unsavedUser));

        assertEquals(2L, user.getId());
        assertEquals("Ivanov", user.getLastName());
        assertEquals("Ivan", user.getFirstName());
        assertEquals("Ivanovych", user.getPatronymic());
        assertEquals("ivan@gmail.com", user.getEmail());
        assertEquals("0961254856", user.getPhoneNumber());
    }

    @Test
    void registrationThrowsExTest() {

        when(userRepository.getUserByEmail(eq("ivan@gmail.com"))).thenReturn(Optional.of(savedUser));

        UserEmailExistsException exception = assertThrows(UserEmailExistsException.class,
                () -> userService.registration(userDto));

        verify(userRepository, times(1)).getUserByEmail(eq("ivan@gmail.com"));
        verify(userMapping, times(0)).convertDtoTo(any());
        verify(userRepository, times(0)).save(any());

        assertEquals("Пользователь с заданным email уже существует", exception.getMessage());
    }

    @Test
    void getTest() throws UserNotFoundException {

        when(userRepository.findById(eq(2L)))
                .thenReturn(Optional.of(savedUser));

        User user = userService.get(2L);

        verify(userRepository, times(1)).findById(eq(2L));

        assertEquals(2, user.getId());
        assertEquals("Ivanov", user.getLastName());
        assertEquals("Ivan", user.getFirstName());
        assertEquals("Ivanovych", user.getPatronymic());
        assertEquals("ivan@gmail.com", user.getEmail());
        assertEquals("0961254856", user.getPhoneNumber());
    }

    @Test
    void getThrowsExTest() {

        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.get(3L));

        verify(userRepository, times(1)).findById(eq(3L));

        assertEquals("Пользователь с заданным id не существует", exception.getMessage());
    }
}