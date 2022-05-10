package com.example.communalpayments.services.interfaces;


import com.example.communalpayments.web.exceptions.UserEmailExistsException;
import com.example.communalpayments.web.exceptions.UserNotFoundException;

public interface UserService {

    void checkUserByEmail(String email) throws UserEmailExistsException;

    void checkUserById(long id) throws UserNotFoundException;
}
