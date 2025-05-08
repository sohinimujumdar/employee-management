//package com.example.employee.controller;
//
//import com.example.employee.entity.User;
//import com.example.employee.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class UserControllerTest {
//
//    @InjectMocks
//    private UserController userController;
//
//    @Mock
//    private UserService userService;
//
//    private User testUser;
//
//    @BeforeEach
//    void setUp() {
//        // Initialize the test user
//        testUser = new User();
//        testUser.setUsername("john_doe");
//        testUser.setPassword("password123");
//        testUser.setRole("USER");
//    }
//
//    @Test
//    void registerUser_shouldReturnCreatedResponse() {
//        // Mock the userService.registerUser() method to simulate registration
////        when(userService.registerUser(testUser)).thenReturn(null); // Assuming void method in service
//        when(userService.registerUser(testUser).thenReturn(void);
//
//        // Call the controller's register method
//        userController.register(testUser);
//
//        // Verify that the service's registerUser method was called
//        verify(userService).registerUser(testUser);
//
//        // Assuming your controller doesn't return a response body, we can assume it will return 200 (OK)
//        // If you want to return a specific response, modify the controller accordingly (e.g. ResponseEntity with message)
//        // Here we just verify that the service was called correctly
//    }
//
//    @Test
//    void registerUser_shouldReturnBadRequestWhenInvalidData() {
//        // Simulate invalid user data (e.g. missing password or username)
//        User invalidUser = new User();  // Missing required fields
//
//        // Call the controller's register method (should trigger validation exception)
//        // In real-world scenario, validation would occur before this point
//        // You can simulate it in test if you have validation annotations
//        // Asserting that an exception is thrown
//    }
//}
