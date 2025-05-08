package com.example.employee.controller;

import com.example.employee.entity.User;
import com.example.employee.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User u1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        u1 = new User();
        u1.setRole(User.Role.USER);
        u1.setPassword("123456");

        u1.setUsername("unit1");
    }

    @Test
    void registerUser_shouldReturnCreatedResponse() {
        when(userService.registerUser(u1)).thenReturn(u1);

        ResponseEntity<String> response = userController.register(u1);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("User registered successfully", response.getBody());
    }
}