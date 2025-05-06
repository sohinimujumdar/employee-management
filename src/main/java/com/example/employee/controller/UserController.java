package com.example.employee.controller;
import com.example.employee.entity.User;
import com.example.employee.service.UserService;
import com.example.employee.entity.*;
import com.example.employee.service.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public void register(@RequestBody User user) {
        userService.registerUser(user);
    }
}