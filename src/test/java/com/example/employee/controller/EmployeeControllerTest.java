

package com.example.employee.controller;

import com.example.employee.entity.Employee;
import com.example.employee.service.EmployeeService;
import com.example.employee.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmployeeControllerTest {
    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private Employee e1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        e1 = new Employee();
        e1.setName("John");
        e1.setAddress("fsdahg");
        e1.setOwnerUsername("john");
        e1.setId(5l);

    }

    @Test
    void registerEmployee_shouldReturnCreatedEmployee() {
        when(employeeService.createEmployee(e1)).thenReturn(e1);

        ResponseEntity<Employee> response = employeeController.create(e1);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(e1, response.getBody());
    }
    @Test
    void getAllEmployees() {
        List<Employee> L1 = new ArrayList<>();
        L1.add(e1);
        L1.add(e1);
        when(employeeService.getEmployees()).thenReturn(L1);

        List<Employee> response = employeeController.getAll();

        assertEquals(2, response.size());
//        verify(employeeService).getEmployees("jack", true);
    }
}


