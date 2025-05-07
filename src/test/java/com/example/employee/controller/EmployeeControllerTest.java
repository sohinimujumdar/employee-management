package com.example.employee.controller;

import com.example.employee.dto.ContactUpdateRequest;
import com.example.employee.entity.Employee;
import com.example.employee.service.EmployeeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {

    private EmployeeService employeeService;
    private EmployeeController employeeController;
    private Authentication mockAuth;

    @BeforeEach
    void setUp() {
        employeeService = Mockito.mock(EmployeeService.class);
        employeeController = new EmployeeController(employeeService);
        mockAuth = Mockito.mock(Authentication.class);
    }

    @AfterEach
    void tearDown() {
        employeeService = null;
        employeeController = null;
    }

    @Test
    void create() {
        Employee emp = new Employee(1L, "Alice", "123 Lane", 12000.00, "jdhjdkh","234345345","adfdf");
        when(mockAuth.getName()).thenReturn("alice");
        when(mockAuth.getAuthorities()).thenReturn(List.of(() -> "ROLE_USER"));
        when(employeeService.createEmployee(any(Employee.class), eq(true))).thenReturn(emp);

        Employee created = employeeController.create(emp, mockAuth);

        assertEquals("Alice", created.getName());
        verify(employeeService, times(1)).createEmployee(any(Employee.class), eq(true));
    }
    @Test
    void getAll() {
        when(mockAuth.getName()).thenReturn("alice");
        when(mockAuth.getAuthorities()).thenReturn(List.of(() -> "ROLE_USER"));

        List<Employee> employees = List.of(new Employee(1L, "Alice", "alice@example.com", "123 Lane", "9999999999", 50000.0));
        when(employeeService.getEmployees("alice", false)).thenReturn(employees);

        List<Employee> result = employeeController.getAll(mockAuth);

        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getName());
    }

    @Test
    void deleteEmployee() {
        doNothing().when(employeeService).deleteEmployee(1L, "bob", false);
        when(mockAuth.getName()).thenReturn("bob");
        when(mockAuth.getAuthorities()).thenReturn(List.of(() -> "ROLE_USER"));

        assertDoesNotThrow(() -> employeeController.deleteEmployee(1L, mockAuth));
        verify(employeeService, times(1)).deleteEmployee(1L, "bob", false);
    }

    @Test
    void updateContactDetails() {
        ContactUpdateRequest request = new ContactUpdateRequest("8888888888", "New Address");
        when(mockAuth.getName()).thenReturn("charlie");

        Employee updated = new Employee(1L, "Charlie", "charlie@example.com", "New Address", "8888888888", 45000.0);
        when(employeeService.updateContactDetails(1L, "8888888888", "New Address", "charlie")).thenReturn(updated);

        Employee result = employeeController.updateContactDetails(1L, request, mockAuth);

        assertEquals("New Address", result.getAddress());
        assertEquals("8888888888", result.getPhone());
    }
}
