package com.example.communalpayments.web.mappings;


import com.example.communalpayments.entities.User;
import com.example.communalpayments.web.dto.UserDto;
import org.springframework.stereotype.Service;


@Service
public class UserMapping implements Mapping<UserDto, User> {

    @Override
    public User convertDto(UserDto userDto) {
        User user = new User();
        user.setLastName(userDto.getLastName());
        user.setFirstName(userDto.getFirstName());
        user.setPatronymic(userDto.getPatronymic());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        return user;
    }
}
