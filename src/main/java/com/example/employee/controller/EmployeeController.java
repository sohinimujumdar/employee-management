package com.example.employee.controller;

import com.example.employee.dto.ContactUpdateRequest;
import com.example.employee.entity.Employee;
import com.example.employee.exception.UnauthorizedAccessException;
import com.example.employee.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/admin")
    public class AdminController {

        @GetMapping("/dashboard")
        public String dashboard() {
            return "admin-dashboard";
        }
    }

    // Create new employee - Only Admin
    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {

        if (employee == null) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        Employee createdEmployee = employeeService.createEmployee(employee); // Call service to create
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED); // 201 Created with body
    }

    //Get all users
    @GetMapping
    public List<Employee> getAll() {
        return employeeService.getEmployees();
    }

    // Update salary by ID — Admin or the employee can update their salary
    @PutMapping("/{id}/salary")
    public ResponseEntity<Employee> updateSalary(
            @PathVariable Long id,
            @RequestParam Double newSalary
    ) {
        try {
            Employee updatedEmployee = employeeService.updateSalary(id, newSalary);
            return ResponseEntity.ok(updatedEmployee); // 200 OK with body
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
    }

    // Delete employee — no access check
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build(); // Returns 204 No Content
    }


    // Update contact details — Admin or the employee can update their own details
    @PutMapping("/{id}/contact")
    public ResponseEntity<Employee> updateContactDetails(
            @PathVariable Long id,
            @RequestBody ContactUpdateRequest contactRequest
    ) {
        try {
            Employee updatedEmployee = employeeService.updateContactDetails(
                    id,
                    contactRequest.getPhone(),
                    contactRequest.getAddress()
            );
            return ResponseEntity.ok(updatedEmployee);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
