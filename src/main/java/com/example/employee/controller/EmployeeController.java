package com.example.employee.controller;

import com.example.employee.entity.Employee;
import com.example.employee.service.EmployeeService;
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

    // Create new employee (only by admin typically)
    @PostMapping
    public Employee create(@RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    // Get employees — Admin sees all, User also sees all (your request)
    @GetMapping
    public List<Employee> getAll(Principal principal) {
        boolean isAdmin = principal.toString().contains("ROLE_ADMIN");
        return employeeService.getEmployees(principal.getName(), isAdmin);
    }

    // Update salary by ID — Admin or owner can update
    @PutMapping("/{id}/salary")
    public Employee updateSalary(
            @PathVariable Long id,
            @RequestParam Double newSalary,
            Principal principal
    ) {
        boolean isAdmin = principal.toString().contains("ROLE_ADMIN");
        return employeeService.updateSalary(id, newSalary, principal.getName(), isAdmin);
    }

    // Delete employee — Admin or owner can delete
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id, Principal principal) {
        boolean isAdmin = principal.toString().contains("ROLE_ADMIN");
        employeeService.deleteEmployee(id, principal.getName(), isAdmin);
    }

    //Update phone and address — Only by the owner
    @PutMapping("/{id}/contact")
    public Employee updateContactDetails(
            @PathVariable Long id,
            @RequestParam String phone,
            @RequestParam String address,
            Principal principal
    ) {
        return employeeService.updateContactDetails(id, phone, address, principal.getName());
    }
}
