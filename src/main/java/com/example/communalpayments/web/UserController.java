package com.example.communalpayments.web;

import com.example.communalpayments.entities.User;
import com.example.communalpayments.services.UserServiceImpl;
import com.example.communalpayments.web.dto.UserDto;
import com.example.communalpayments.web.exceptions.UserEmailExistsException;
import com.example.communalpayments.web.exceptions.UserNotFoundException;
import com.example.communalpayments.web.utils.UserMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserServiceImpl userService;
    private final UserMapping userMapping;

    @Autowired
    public UserController(UserServiceImpl userService, UserMapping userMapping) {
        this.userService = userService;
        this.userMapping = userMapping;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody UserDto userDto) {
        try {
            userService.checkUserByEmail(userDto.getEmail());
            User user = userMapping.convertDtoTo(userDto);
            userService.save(user);
            return new ResponseEntity<>("id : " + user.getId(), HttpStatus.CREATED);
        } catch (UserEmailExistsException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable long id) {
        try {
            User user = userService.get(id);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
