package com.example.employee.controller;
import com.example.employee.entity.User;
import com.example.employee.service.UserService;
import com.sun.net.httpserver.Authenticator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Request body cannot be null");
        }

        userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully");
    }
    @PostMapping("/login")
    public String login(@RequestBody User user) throws Exception {
        AuthenticationManager authManager = authenticationManager(); // get from config
        return userService.verify(user, authManager);
    }

    // You’ll need to autowire it or inject it via constructor from SecurityConfig
    @Autowired
    private AuthenticationConfiguration authConfig;

    private AuthenticationManager authenticationManager() throws Exception {
        return authConfig.getAuthenticationManager();
    }

}