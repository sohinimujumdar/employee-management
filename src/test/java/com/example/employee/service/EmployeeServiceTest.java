package com.example.employee.service;

import com.example.employee.entity.Employee;
import com.example.employee.repository.EmployeeRepository;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    private void mockAuthenticatedUser(String username) {
        Authentication auth = Mockito.mock(Authentication.class);
        when(auth.getName()).thenReturn(username);

        SecurityContext context = Mockito.mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);

        SecurityContextHolder.setContext(context);
    }

    @Test
    void testCreateEmployee() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setOwnerUsername("johnnydoe");

        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee savedEmployee = employeeService.createEmployee(employee);

        assertEquals(employee, savedEmployee);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testGetEmployees() {
        Employee emp1 = new Employee();
        emp1.setId(1L);
        emp1.setName("John Doe");

        Employee emp2 = new Employee();
        emp2.setId(2L);
        emp2.setName("Jane Smith");

        List<Employee> mockEmployeeList = Arrays.asList(emp1, emp2);
        when(employeeRepository.findAll()).thenReturn(mockEmployeeList);

        List<Employee> employees = employeeService.getEmployees();

        assertEquals(2, employees.size());
        assertEquals("John Doe", employees.get(0).getName());
        assertEquals("Jane Smith", employees.get(1).getName());

        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testUpdateSalary() {
        Long employeeId = 1L;
        Double newSalary = 90000.0;
        Employee existingEmployee = new Employee();
        existingEmployee.setId(employeeId);
        existingEmployee.setSalary(70000.0);
        existingEmployee.setName("Ram");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Employee updatedEmployee = employeeService.updateSalary(employeeId, newSalary);

        assertEquals(newSalary, updatedEmployee.getSalary());
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).save(existingEmployee);
    }

    @Test
    void testDeleteEmployee() {
        long employeeId = 1L;
        Employee existingEmployee = new Employee();
        existingEmployee.setId(employeeId);
        existingEmployee.setName("Alice");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));

        employeeService.deleteEmployee(employeeId);

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).delete(existingEmployee);
    }

    @Test
    void testUpdateContactDetails_Success() throws AccessDeniedException {
        Long employeeId = 1L;
        String username = "john_doe";
        mockAuthenticatedUser(username);

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setOwnerUsername(username);
        employee.setPhone("123");
        employee.setAddress("Old Address");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        Employee updated = employeeService.updateContactDetails(1L, "999999", "New Address");

        assertEquals("999999", updated.getPhone());
        assertEquals("New Address", updated.getAddress());
        verify(employeeRepository).save(employee);
    }

    @Test
    void testUpdateContactDetails_AccessDenied() {
        Long employeeId = 1L;
        mockAuthenticatedUser("someone_else");

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setOwnerUsername("real_owner");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        assertThrows(AccessDeniedException.class, () ->
                employeeService.updateContactDetails(employeeId, "123", "Street"));

        verify(employeeRepository, never()).save(any());
    }
}
