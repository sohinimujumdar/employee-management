//package com.example.employee.controller;
//
//import com.example.employee.entity.Employee;
//import com.example.employee.service.EmployeeService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.Collections;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//class EmployeeControllerTest {
//
//    @Mock
//    private EmployeeService employeeService;  // Mocked EmployeeService
//
//    @Mock
//    private Authentication authentication;
//
//    @InjectMocks
//    private EmployeeController employeeController;  // Inject mocked services into the controller
//
//    @BeforeEach
//    void setUp() {
//        // Set up the test context if necessary
//        SecurityContextHolder.setContext(mock(SecurityContext.class)); // Mocking Security Context
//
//    }
//
//    @Test
//    void createEmployee_asAdmin_shouldReturnCreatedEmployee() {
//        // Arrange
//        Employee inputEmployee = new Employee();
//        inputEmployee.setName("Alice");
//        inputEmployee.setRole("Engineer");
//
//        Employee createdEmployee = new Employee();
//        createdEmployee.setId(1L);
//        createdEmployee.setName("Alice");
//        createdEmployee.setRole("Engineer");
//
//        // Mock the authentication to have ROLE_ADMIN
//        GrantedAuthority adminAuthority = () -> "ROLE_ADMIN";
//
//        // Mock service call with any(Employee.class) to avoid exact object match
//        when(employeeService.createEmployee(any(Employee.class), eq(true)))
//                .thenReturn(createdEmployee);
//
//        // Act
//        Employee result = employeeController.create(inputEmployee, authentication);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(1L, result.getId());
//        assertEquals("Alice", result.getName());
//        assertEquals("Engineer", result.getRole());
//
//        verify(employeeService, times(1)).createEmployee(any(Employee.class), eq(true));
//    }
//}
//
////    @Test
////    void create_Success() {
////        // Create a sample employee
////        Employee emp = new Employee(1L, "Alice", "123 Lane", 12000.00, "jdhjdkh", "234345345", "adfdf");
////
////        // Mock the createEmployee method of EmployeeService to return the sample employee
////        when(employeeService.createEmployee(any(Employee.class), eq(true))).thenReturn(emp);
////
////        // Mock the Authentication object (for example, for an admin role)
////        Authentication mockAuth = mock(Authentication.class);
////        when(mockAuth.getName()).thenReturn("admin"); // Set a mocked username (or role) here
////
////        // Call the controller's create method with the mock Authentication
////        Employee created = employeeController.create(emp, mockAuth);
////
////        // Assert that the created employee's name is "Alice"
////        assertEquals("Alice", created.getName());
////
////        // Optionally, verify that the service method was called once
////        verify(employeeService, times(1)).createEmployee(any(Employee.class), eq(true));
////    }
////}
