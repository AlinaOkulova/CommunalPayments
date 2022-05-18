package com.example.communalpayments.services;

import com.example.communalpayments.dao.UserRepository;
import com.example.communalpayments.entities.User;
import com.example.communalpayments.exceptions.UserEmailExistsException;
import com.example.communalpayments.exceptions.UserNotFoundException;
import com.example.communalpayments.services.interfaces.UserService;
import com.example.communalpayments.web.dto.UserDto;
import com.example.communalpayments.web.mappings.UserMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private UserMapping userMapping;

    private UserService userService;

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

        doReturn(unsavedUser).when(userMapping).convertDto(eq(userDto));
        doReturn(savedUser).when(userRepository).save(eq(unsavedUser));

        User user = userService.registration(userDto);

        verify(userMapping, times(1)).convertDto(eq(userDto));

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
        verify(userMapping, times(0)).convertDto(any());
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

    @Test
    void updateUserTest() throws UserNotFoundException, UserEmailExistsException {

        when(userRepository.existsById(2L)).thenReturn(true);
        when(userRepository.getUserByEmail(eq("ivan@gmail.com"))).thenReturn(Optional.of(savedUser));
        when(userRepository.save(eq(User.builder()
                .id(2L)
                .lastName("Ivanov")
                .firstName("Ivan")
                .patronymic("Ivanovych")
                .email("ivan@gmail.com")
                .phoneNumber("0960000001")
                .build()))).thenReturn(User.builder()
                .id(2L)
                .lastName("Ivanov")
                .firstName("Ivan")
                .patronymic("Ivanovych")
                .email("ivan@gmail.com")
                .phoneNumber("0960000001")
                .build());

        User updatedUser = userService.updateUser(User.builder()
                .id(2L)
                .lastName("Ivanov")
                .firstName("Ivan")
                .patronymic("Ivanovych")
                .email("ivan@gmail.com")
                .phoneNumber("0960000001")
                .build());

        verify(userRepository, times(1)).existsById(eq(2L));
        verify(userRepository, times(1)).getUserByEmail(eq("ivan@gmail.com"));
        verify(userRepository, times(1)).save(eq(User.builder()
                .id(2L)
                .lastName("Ivanov")
                .firstName("Ivan")
                .patronymic("Ivanovych")
                .email("ivan@gmail.com")
                .phoneNumber("0960000001")
                .build()));

        assertEquals(2, updatedUser.getId());
        assertEquals("Ivanov", updatedUser.getLastName());
        assertEquals("Ivan", updatedUser.getFirstName());
        assertEquals("Ivanovych", updatedUser.getPatronymic());
        assertEquals("ivan@gmail.com", updatedUser.getEmail());
        assertEquals("0960000001", updatedUser.getPhoneNumber());
    }

    @Test
    void updateUserThrowsUserNotFoundExTest() {

        when(userRepository.existsById(2L)).thenReturn(false);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.updateUser(User.builder()
                        .id(2L)
                        .lastName("Ivanov")
                        .firstName("Ivan")
                        .patronymic("Ivanovych")
                        .email("ivan@gmail.com")
                        .phoneNumber("0960000001")
                        .build()));

        verify(userRepository, times(1)).existsById(eq(2L));
        verify(userRepository, times(0)).getUserByEmail(any());
        verify(userRepository, times(0)).save(any(User.class));

        assertEquals("Пользователь с заданным id не существует", exception.getMessage());
    }

    @Test
    void updateUserThrowsUserEmailExistsExTest() {
        when(userRepository.existsById(2L)).thenReturn(true);
        when(userRepository.getUserByEmail("ivan@gmail.com")).thenReturn(Optional.of(User.builder()
                .id(10L)
                .lastName("Semenov")
                .firstName("Ivan")
                .patronymic("Semenovych")
                .email("ivan@gmail.com")
                .phoneNumber("0730230056")
                .build()));

        UserEmailExistsException exception = assertThrows(UserEmailExistsException.class,
                () -> userService.updateUser(User.builder()
                        .id(2L)
                        .lastName("Ivanov")
                        .firstName("Ivan")
                        .patronymic("Ivanovych")
                        .email("ivan@gmail.com")
                        .phoneNumber("0960000001")
                        .build()));

        verify(userRepository, times(1)).existsById(eq(2L));
        verify(userRepository, times(1)).getUserByEmail(eq("ivan@gmail.com"));
        verify(userRepository, times(0)).save(eq(User.builder()
                .id(2L)
                .lastName("Ivanov")
                .firstName("Ivan")
                .patronymic("Ivanovych")
                .email("ivan@gmail.com")
                .phoneNumber("0960000001")
                .build()));

        assertEquals("Пользователь с заданным email уже существует", exception.getMessage());
    }
}