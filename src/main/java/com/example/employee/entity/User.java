package com.example.employee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class User {

    @Id
    private String username;

    private String password;

    private String email;

    private String role; // Change this to String if you want to store role as a String
}
