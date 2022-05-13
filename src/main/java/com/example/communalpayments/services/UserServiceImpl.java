package com.example.communalpayments.services;

import com.example.communalpayments.dao.UserRepository;
import com.example.communalpayments.entities.User;
import com.example.communalpayments.exceptions.UserEmailExistsException;
import com.example.communalpayments.exceptions.UserNotFoundException;
import com.example.communalpayments.services.interfaces.GetService;
import com.example.communalpayments.services.interfaces.UserService;
import com.example.communalpayments.web.dto.UserDto;
import com.example.communalpayments.web.mappings.UserMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements GetService<User, Long>, UserService {

    private final UserRepository userRepository;
    private final UserMapping mapping;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapping mapping) {
        this.userRepository = userRepository;
        this.mapping = mapping;
    }

    @Override
    public User registration(UserDto userDto) throws UserEmailExistsException {
        checkUserByEmail(userDto.getEmail());
        User user = userRepository.save(mapping.convertDtoTo(userDto));
        log.info("Сохранил пользователя: " + user);
        return user;
    }

    @Override
    public User get(Long userId) throws UserNotFoundException {
        Optional<User> optional = userRepository.findById(userId);
        if (optional.isPresent()) {
            return optional.get();
        } else throw new UserNotFoundException("Пользователь с заданным id не существует");
    }

    private void checkUserByEmail(String email) throws UserEmailExistsException {
        Optional<User> optional = userRepository.getUserByEmail(email);
        if (optional.isPresent()) {
            throw new UserEmailExistsException("Пользователь с заданным email уже существует");
        }
    }
}
