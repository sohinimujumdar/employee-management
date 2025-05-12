package com.example.employee.exception;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ErrorDetails {
    private int statusCode;
    private String message;

    public ErrorDetails(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
