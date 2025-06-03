package com.example.employee.controller;

import com.example.employee.entity.Employee;
import com.example.employee.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@Controller
@RequestMapping("/employees")
public class WebController {

    private final EmployeeService employeeService;

    public WebController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String viewEmployees(Model model) {
        model.addAttribute("employees", employeeService.getEmployees());
        return "employees";
    }

    @GetMapping("/{id}")
    public String viewEmployee(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        return "employee-details";
    }
}
