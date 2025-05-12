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
}
