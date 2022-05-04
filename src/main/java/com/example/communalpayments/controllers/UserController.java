package com.example.communalpayments.controllers;

import com.example.communalpayments.entities.User;
import com.example.communalpayments.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<User> registration(@RequestBody User user) {
        userService.save(user);

        return ResponseEntity.ok(user);
    }


}
