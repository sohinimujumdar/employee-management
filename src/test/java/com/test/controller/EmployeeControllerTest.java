package com.test.controller;

import com.example.employee.entity.Employee;
import com.example.employee.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import com.example.employee.controller.EmployeeController;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeService employeeService;

    @Test
    public void testGetEmployeeById_Success() throws Exception {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");

        when(employeeService.getEmployeeById(1L)).thenReturn(employee);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    // Use @TestConfiguration to define a custom mock bean
    @TestConfiguration
    static class TestConfig {
        @Bean
        public EmployeeService employeeService() {
            EmployeeService mockService = mock(EmployeeService.class);
            return mockService;
        }
    }
}
