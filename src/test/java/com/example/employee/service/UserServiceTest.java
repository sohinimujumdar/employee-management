package com.example.employee.service;

import com.example.employee.entity.User;
import com.example.employee.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static com.example.employee.entity.User.Role.ADMIN;
import static com.example.employee.entity.User.Role.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService; // Automatically injects userRepository

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails() {
        // Arrange
        User mockUser = new User();
        mockUser.setUsername("ram");
        mockUser.setPassword("password123");
        mockUser.setRole(USER);
        when(userRepository.findByUsername("ram")).thenReturn(Optional.of(mockUser));

        // Act
        UserDetails userDetails = userService.loadUserByUsername("ram");

        // Assert
        assertNotNull(userDetails);
        assertEquals("ram", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

//    @Test
//    void loadUserByUsername_shouldThrowExceptionIfUserNotFound() {
//        // Arrange
//        when(userRepository.findByUsername()).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(UsernameNotFoundException.class, () -> {
//            userService.loadUserByUsername("unknown");
//        });
//    }
}
