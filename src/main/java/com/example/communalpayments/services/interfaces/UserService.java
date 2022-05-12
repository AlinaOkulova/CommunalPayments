package com.example.communalpayments.services.interfaces;


import com.example.communalpayments.entities.User;
import com.example.communalpayments.exceptions.UserEmailExistsException;
import com.example.communalpayments.web.dto.UserDto;

public interface UserService {

    User registration(UserDto userDto) throws UserEmailExistsException;
}
