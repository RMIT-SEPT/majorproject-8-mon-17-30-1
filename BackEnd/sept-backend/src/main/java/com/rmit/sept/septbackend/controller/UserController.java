package com.rmit.sept.septbackend.controller;

import com.rmit.sept.septbackend.entity.User;
import com.rmit.sept.septbackend.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{user-id}")
    public User getUser(@PathVariable(name = "user-id") Integer userId) {
        return userService.getUserById(userId);
    }

}
