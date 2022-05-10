package com.example.communalpayments.services;

import com.example.communalpayments.dao.UserRepository;
import com.example.communalpayments.entities.User;
import com.example.communalpayments.services.interfaces.Service;
import com.example.communalpayments.services.interfaces.UserService;
import com.example.communalpayments.exceptions.UserEmailExistsException;
import com.example.communalpayments.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@org.springframework.stereotype.Service
public class UserServiceImpl implements Service<User, Long>, UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User get(Long userId) throws UserNotFoundException {
        Optional<User> optional = userRepository.findById(userId);
        if (optional.isPresent()) {
            return optional.get();
        } else throw new UserNotFoundException("Пользователь с заданным id не существует");
    }

    @Override
    public void checkUserByEmail(String email) throws UserEmailExistsException {
        Optional<User> optional = userRepository.getUserByEmail(email);
        if (optional.isPresent()) {
            throw new UserEmailExistsException("Пользователь с заданным email уже существует");
        }
    }

    @Override
    public void checkUserById(long id) throws UserNotFoundException {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isEmpty()) throw new UserNotFoundException("Пользователь с заданным id не существует");
    }
}
