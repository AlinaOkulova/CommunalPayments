package com.example.communalpayments.services;

import com.example.communalpayments.dao.UserRepository;
import com.example.communalpayments.entities.User;
import com.example.communalpayments.exceptions.UserEmailExistsException;
import com.example.communalpayments.web.dto.UserDto;
import com.example.communalpayments.web.mappings.UserMapping;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private UserMapping userMapping;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userMapping = Mockito.mock(UserMapping.class);
        userService = new UserServiceImpl(userRepository, userMapping);
    }

    @AfterEach
    void tearDown() {
    }

    @SneakyThrows
    @Test()
    void registrationTest() {

        doReturn(User.builder()
                .lastName("Ivanov")
                .firstName("Ivan")
                .patronymic("Ivanovych")
                .email("ivan@gmail.com")
                .phoneNumber("0961254856")
                .build())
                .when(userMapping)
                .convertDtoTo(eq(new UserDto("Ivanov", "Ivan",
                        "Ivanovych", "ivan@gmail.com", "0961254856")));

        doReturn(User.builder()
                .id(4L)
                .lastName("Ivanov")
                .firstName("Ivan")
                .patronymic("Ivanovych")
                .email("ivan@gmail.com")
                .phoneNumber("0961254856")
                .build())
                .when(userRepository)
                .save(eq(User.builder()
                        .id(0L)
                        .lastName("Ivanov")
                        .firstName("Ivan")
                        .patronymic("Ivanovych")
                        .email("ivan@gmail.com")
                        .phoneNumber("0961254856")
                        .build()));

        User user = userService.registration(new UserDto("Ivanov", "Ivan",
                "Ivanovych", "ivan@gmail.com", "0961254856"));

        verify(userMapping, times(1))
                .convertDtoTo(eq(new UserDto("Ivanov", "Ivan",
                        "Ivanovych", "ivan@gmail.com", "0961254856")));

        verify(userRepository, times(1))
                .save(eq(User.builder()
                        .lastName("Ivanov")
                        .firstName("Ivan")
                        .patronymic("Ivanovych")
                        .email("ivan@gmail.com")
                        .phoneNumber("0961254856")
                        .build()));

        assertEquals(4L, user.getId());
        assertEquals("Ivanov", user.getLastName());
        assertEquals("Ivan", user.getFirstName());
        assertEquals("Ivanovych", user.getPatronymic());
        assertEquals("ivan@gmail.com", user.getEmail());
        assertEquals("0961254856", user.getPhoneNumber());
    }



    private static List<User> getUsers() {
        List<User> users = new ArrayList<>();
        users.add(User.builder().id(1).firstName("Alina").email("alina@gmail.com").build());
        users.add(User.builder().id(2).firstName("Roman").email("roman@gmail.com").build());
        users.add(User.builder().id(3).firstName("Taras").email("taras@gmail.com").build());
        return users;
    }
}