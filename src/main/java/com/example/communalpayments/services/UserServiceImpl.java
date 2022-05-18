package com.example.communalpayments.services;

import com.example.communalpayments.dao.UserRepository;
import com.example.communalpayments.entities.User;
import com.example.communalpayments.exceptions.UserEmailExistsException;
import com.example.communalpayments.exceptions.UserNotFoundException;
import com.example.communalpayments.services.interfaces.UserService;
import com.example.communalpayments.web.dto.UserDto;
import com.example.communalpayments.web.mappings.UserMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapping mapping;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapping mapping) {
        this.userRepository = userRepository;
        this.mapping = mapping;
    }

    @Override
    public User registration(UserDto userDto) throws UserEmailExistsException {
        Optional<User> optional = userRepository.getUserByEmail(userDto.getEmail());
        if (optional.isPresent()) {
            throw new UserEmailExistsException();
        }
        User user = userRepository.save(mapping.convertDto(userDto));
        log.info("Сохранил пользователя: " + user);
        return user;
    }

    @Override
    public User get(Long userId) throws UserNotFoundException {
        Optional<User> optional = userRepository.findById(userId);
        if (optional.isPresent()) {
            return optional.get();
        } else throw new UserNotFoundException();
    }

    @Override
    public User updateUser(User user) throws UserNotFoundException, UserEmailExistsException {
        if (userRepository.existsById(user.getId())) {
            Optional<User> optional = userRepository.getUserByEmail(user.getEmail());
            if (optional.isPresent()) {
                if (optional.get().getId() == user.getId()) {
                    return userRepository.save(user);
                } else throw new UserEmailExistsException();
            } else return userRepository.save(user);
        } else throw new UserNotFoundException();
    }
}
