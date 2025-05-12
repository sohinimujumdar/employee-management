package com.example.employee.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter

public class ContactUpdateRequest {
    private String phone;
    private String address;

    public ContactUpdateRequest(String phone, String address) {
        this.phone = phone;
        this.address = address;
    }
}
