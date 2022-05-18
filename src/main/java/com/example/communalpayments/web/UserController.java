package com.example.communalpayments.web;

import com.example.communalpayments.entities.User;
import com.example.communalpayments.exceptions.UserEmailExistsException;
import com.example.communalpayments.exceptions.UserNotFoundException;
import com.example.communalpayments.services.interfaces.UserService;
import com.example.communalpayments.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@Valid @RequestBody UserDto userDto)
            throws UserEmailExistsException {

        User user = userService.registration(userDto);
        return new ResponseEntity<>("{\n\"id\" : " + user.getId() + "\n}", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) throws UserNotFoundException {

        User user = userService.get(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user)
            throws UserNotFoundException, UserEmailExistsException {

        return ResponseEntity.ok(userService.updateUser(user));
    }
}
