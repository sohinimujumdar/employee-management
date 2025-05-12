

package com.example.employee.controller;

import com.example.employee.dto.ContactUpdateRequest;
import com.example.employee.entity.Employee;
import com.example.employee.service.EmployeeService;
import com.example.employee.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
//update salary
@Test
void updateSalary_shouldReturnUpdatedEmployee() {
    Long employeeId = 1L;
    Double newSalary = 75000.0;

    Employee updatedEmployee = new Employee();
    updatedEmployee.setId(employeeId);
    updatedEmployee.setSalary(newSalary);

    when(employeeService.updateSalary(employeeId, newSalary)).thenReturn(updatedEmployee);

    ResponseEntity<Employee> response = employeeController.updateSalary(employeeId, newSalary);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(updatedEmployee, response.getBody());
    verify(employeeService).updateSalary(employeeId, newSalary);
}
    @Test
    void deleteEmployee_shouldReturnNoContent() {
        Long employeeId = 1L;

        // Simulate the service layer deletion
        doNothing().when(employeeService).deleteEmployee(employeeId);

        ResponseEntity<Void> response = employeeController.deleteEmployee(employeeId);

        // Assert that the status code is 204 No Content
        assertEquals(204, response.getStatusCodeValue());

        // Verify that the service method was called
        verify(employeeService, times(1)).deleteEmployee(employeeId);
    }

    @Test
    void updateContactDetails_shouldReturnUpdatedEmployee() throws AccessDeniedException {
        // Given
        Long employeeId = 1L;
        String newPhone = "1234567890";
        String newAddress = "123 New Street";

        // Create request object
        ContactUpdateRequest request = new ContactUpdateRequest(newPhone, newAddress);

        // Create updated employee object that should be returned by the service
        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(employeeId);
        updatedEmployee.setPhone(newPhone);
        updatedEmployee.setAddress(newAddress);

        // Mocking the service method
        when(employeeService.updateContactDetails(employeeId, newPhone, newAddress)).thenReturn(updatedEmployee);

        // When calling the controller method
        ResponseEntity<Employee> response = employeeController.updateContactDetails(employeeId, request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());  // Check that status is OK (200)
        assertEquals(updatedEmployee, response.getBody());     // Check that the body contains the updated employee
        verify(employeeService).updateContactDetails(employeeId, newPhone, newAddress);  // Verify the service method was called
    }

}



