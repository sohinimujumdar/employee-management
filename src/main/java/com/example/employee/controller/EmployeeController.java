package com.example.employee.controller;

import com.example.employee.dto.ContactUpdateRequest;
import com.example.employee.entity.Employee;
import com.example.employee.service.EmployeeService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Create new employee - Only Admin
    @PostMapping
    public Employee create(@RequestBody Employee employee, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        return employeeService.createEmployee(employee, isAdmin);
    }

    // Get employees — Admin sees all, User only sees their own details
    @GetMapping
    public List<Employee> getAll(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        String username = authentication.getName();

        // If admin, get all employees, if user, get only their own
        return employeeService.getEmployees(username, isAdmin);
    }

    // Update salary by ID — Admin or the employee can update their salary
    @PutMapping("/{id}/salary")
    public Employee updateSalary(
            @PathVariable Long id,
            @RequestParam Double newSalary,
            Authentication authentication
    ) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        // Ensure employee can only update their own salary unless they are an admin
        return employeeService.updateSalary(id, newSalary, isAdmin);
    }

    // Delete employee — Admin or the employee can delete their own details
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        // Ensure only the admin or the employee themselves can delete the employee
        employeeService.deleteEmployee(id, authentication.getName(), isAdmin);
    }

    // Update contact details — Admin or the employee can update their own details
    @PutMapping("/{id}/contact")
    public Employee updateContactDetails(
            @PathVariable Long id,
            @RequestBody ContactUpdateRequest contactRequest,
            Authentication authentication
    ) {
        // Ensure only the employee or admin can update contact details
        return employeeService.updateContactDetails(
                id,
                contactRequest.getPhone(),
                contactRequest.getAddress(),
                authentication.getName()
        );
    }
}
