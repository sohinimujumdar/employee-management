package com.example.employee.controller;

import com.example.employee.dto.UserRegistrationResponse;
import com.example.employee.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import static com.example.employee.entity.User.Role.USER;

@RestController
@RequestMapping("/api/users")

public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User registered successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserRegistrationResponse.class),
                                    examples = @ExampleObject(value = """
                                                {
                                                    "message": "User registered successfully",
                                                    "username": "sohini"
                                                }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = "{\"message\": \"Invalid input\"}")
                            )
                    )
            }
    )
    ResponseEntity<String> register(@RequestBody User user) {
        log.info("Received request to /register with params: {}", user);

        try {
            // Mock behavior: set password and role
            user.setPassword("$2a$10$WszV.c9kENam3dGxO1s5N.Rr5tpQoIA7E3rDRj3h8HT5sg7QfQuIq");
            user.setRole(USER);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User registered successfully with username: " + user.getUsername());

        } catch (Exception e) {
            log.error("Error during user registration: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to register user due to internal error.");
        }
    }
}
