package com.example.communalpayments.services.interfaces;


import com.example.communalpayments.entities.User;
import com.example.communalpayments.exceptions.UserEmailExistsException;
import com.example.communalpayments.exceptions.UserNotFoundException;
import com.example.communalpayments.web.dto.UserDto;

public interface UserService extends GetService<User, Long> {

    User get(Long id) throws UserNotFoundException;

    User registration(UserDto userDto) throws UserEmailExistsException;

    User updateUser(User user) throws UserNotFoundException, UserEmailExistsException;
}
