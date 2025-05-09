package com.example.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserDTO {
    @Getter @Setter

    private String username;
    private String password;
    private String role;

//    // Required: Getters
//    public String getUsername() {
//        return username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    // Optional: Setters (if you use setters or need deserialization)
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }
}
