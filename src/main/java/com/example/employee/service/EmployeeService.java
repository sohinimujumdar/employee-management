package com.example.employee.service;

import com.example.employee.entity.Employee;
import com.example.employee.exception.EmployeeNotFoundException;
import com.example.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Create a new employee
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Get employees - all if admin, all if user (per your new requirement)
    public List<Employee> getEmployees(String username, boolean isAdmin) {
        return employeeRepository.findAll();
    }

    // Get employee by ID
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + id + " not found"));
    }

    // Update salary (checks for admin or owner)
    public Employee updateSalary(Long id, Double newSalary, String username, boolean isAdmin) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + id + " not found"));

        String normalizedUsername = username.trim().toLowerCase();
        String normalizedOwnerUsername = emp.getOwnerUsername().trim().toLowerCase();

        if (isAdmin || normalizedOwnerUsername.equals(normalizedUsername)) {
            emp.setSalary(newSalary);
            return employeeRepository.save(emp);
        }

        throw new SecurityException("Not authorized");
    }

    // Delete employee (checks for admin or owner)
    public void deleteEmployee(Long id, String username, boolean isAdmin) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + id + " not found"));

        if (isAdmin || emp.getOwnerUsername().equals(username)) {
            employeeRepository.deleteById(id);
        } else {
            throw new SecurityException("Not authorized to delete this employee");
        }
    }

    // ✅ NEW: Update phone and address (only by owner, not admin)
    public Employee updateContactDetails(Long id, String phone, String address, String username) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + id + " not found"));

        String normalizedUsername = username.trim().toLowerCase();
        String normalizedOwnerUsername = emp.getOwnerUsername().trim().toLowerCase();

        if (normalizedOwnerUsername.equals(normalizedUsername)) {
            emp.setPhone(phone);
            emp.setAddress(address);
            return employeeRepository.save(emp);
        }

        throw new SecurityException("Only the employee can update their phone and address.");
    }
}
