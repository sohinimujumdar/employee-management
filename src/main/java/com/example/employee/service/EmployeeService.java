package com.example.employee.service;

import com.example.employee.entity.Employee;
import com.example.employee.exception.EmployeeNotFoundException;
import com.example.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Create a new employee - Only Admin
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    //get all employees
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    // Get employee by ID
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + id + " not found"));
    }

    // Update salary - Only Admin
    public Employee updateSalary(Long id, Double newSalary) {
        Employee emp = getEmployeeById(id);
        emp.setSalary(newSalary);
        return employeeRepository.save(emp);
    }

    // Delete employee - No access checks
    public void deleteEmployee(Long id) {
        Employee emp = getEmployeeById(id); // Ensure employee exists
        employeeRepository.delete(emp);
    }


    // Update phone and address - Only by Owner
    public Employee updateContactDetails(Long id, String phone, String address) throws AccessDeniedException {
        // Get currently authenticated username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Get the employee being updated
        Employee emp = getEmployeeById(id);

        // Check if the username matches
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!emp.getOwnerUsername().equals(currentUsername)) {
            throw new AccessDeniedException("You are not allowed to update this employee's details.");
        }

        // Proceed with update
        if (phone != null && !phone.trim().isEmpty()) {
            emp.setPhone(phone);
        }

        if (address != null && !address.trim().isEmpty()) {
            emp.setAddress(address);
        }

        return employeeRepository.save(emp);
    }
}



