package com.example.employee.service;

import com.example.employee.entity.Employee;
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

    // Get employees - all if admin, only own details if not
    public List<Employee> getEmployees(String username, boolean isAdmin) {
        return isAdmin ? employeeRepository.findAll()
                : employeeRepository.findByOwnerUsername(username);
    }

    // Update salary - only if admin or owner
    public Employee updateSalary(Long id, Double newSalary, String username, boolean isAdmin) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (isAdmin || emp.getOwnerUsername().equals(username)) {
            emp.setSalary(newSalary);
            return employeeRepository.save(emp);
        }

        throw new SecurityException("Not authorized");
    }

    // Delete an employee
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
