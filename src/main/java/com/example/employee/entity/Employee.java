package com.example.employee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private String name;
    @Getter
    private String role;
    private Double salary;
    @Getter
    private String ownerUsername;
    private String phone;
    private String address;

//    // --- Getters and Setters ---
//    public Long getId(long l) {
//        return id;
//    }
//
//    public Double getSalary() {
//        return salary;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }
//
//    public void setSalary(Double salary) {
//        this.salary = salary;
//    }
//
//    public void setOwnerUsername(String ownerUsername) {
//        this.ownerUsername = ownerUsername;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
}
