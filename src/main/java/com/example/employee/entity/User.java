package com.example.employee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
public class User {
    @Getter @Setter
    @Id
    @NotBlank
    private String username;  // This will be the unique ID

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // --- Getters and Setters ---

    public enum Role {
        ADMIN,
        USER
    }
}
