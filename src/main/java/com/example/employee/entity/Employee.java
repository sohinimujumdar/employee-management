package com.example.employee.entity;

import jakarta.persistence.*;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String role;
    private Double salary;

    // 👇 This must exist to track who owns the employee entry
    private String ownerUsername;

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public Double getSalary() {
        return salary;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }
}
