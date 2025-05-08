package com.example.employee.service;

import com.example.employee.entity.Employee;
import com.example.employee.exception.EmployeeNotFoundException;
import com.example.employee.exception.UnauthorizedAccessException;
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

    // Create a new employee - Only Admin
    public Employee createEmployee(Employee employee) {
//        if (!isAdmin) {
//            throw new UnauthorizedAccessException("Only admins can create new employees.");
//        }
        return employeeRepository.save(employee);
    }

    // Get all employees
//    public List<Employee> getEmployees(String username, boolean isAdmin) {
//        return employeeRepository.findAll();
//    }
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    // Get employee by ID
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + id + " not found"));
    }

    // Update salary - Only Admin
    public Employee updateSalary(Long id, Double newSalary, boolean isAdmin) {
        if (!isAdmin) {
            throw new UnauthorizedAccessException("Only admins can update salary.");
        }

        Employee emp = getEmployeeById(id);
        emp.setSalary(newSalary);
        return employeeRepository.save(emp);
    }

    // Delete employee - Admin or Owner
    public void deleteEmployee(Long id, String username, boolean isAdmin) {
        Employee emp = getEmployeeById(id);
        if (isAdmin || emp.getOwnerUsername().equalsIgnoreCase(username)) {
            employeeRepository.deleteById(id);
        } else {
            throw new UnauthorizedAccessException("Not authorized to delete this employee.");
        }
    }

    // Update phone and address - Only by Owner
    public Employee updateContactDetails(Long id, String phone, String address, String username) {
        Employee emp = getEmployeeById(id);

        if (!emp.getOwnerUsername().equalsIgnoreCase(username)) {
            throw new UnauthorizedAccessException("Only the employee can update their phone and address.");
        }

        if (phone != null && !phone.trim().isEmpty()) {
            emp.setPhone(phone);
        }

        if (address != null && !address.trim().isEmpty()) {
            emp.setAddress(address);
        }

        return employeeRepository.save(emp);
    }
}
