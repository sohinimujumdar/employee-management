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

    @PostMapping
    public Employee create(@RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    @GetMapping
    public List<Employee> getAll(Principal principal) {
        boolean isAdmin = principal.toString().contains("ROLE_ADMIN");
        return employeeService.getEmployees(principal.getName(), isAdmin);
    }

    @PutMapping("/{id}/salary")
    public Employee updateSalary(@PathVariable Long id, @RequestBody Double newSalary, Principal principal) {
        boolean isAdmin = principal.toString().contains("ROLE_ADMIN");
        return employeeService.updateSalary(id, newSalary, principal.getName(), isAdmin);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }
}