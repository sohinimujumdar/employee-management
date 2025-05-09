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
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//}
