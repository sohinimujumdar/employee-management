package com.example.employee.controller;

import com.example.employee.dto.ContactUpdateRequest;
import com.example.employee.entity.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Create new employee — mock hardcoded response
    @PostMapping
    @Operation(summary = "Create a new employee", description = "Creates a new employee with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid employee data")
    })
    public ResponseEntity<Employee> create(@RequestBody Employee employee) throws JsonProcessingException {
        log.info("Received request to create employee: {}", employee);

        String json = """
            {
                "id": 100,
                "name": "%s",
                "role": "%s",
                "salary": %.2f,
                "phone": "%s",
                "address": "%s"
            }
        """.formatted(
                employee.getName(),
                employee.getRole(),
                employee.getSalary(),
                employee.getPhone(),
                employee.getAddress()
        );

        Employee mockEmployee = objectMapper.readValue(json, Employee.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(mockEmployee);
    }

    // Get all employees — mock list
    @GetMapping
    @Operation(summary = "Get all employees", description = "Returns a list of all employees.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of employees returned successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error when fetching employees")
    })
    public ResponseEntity<List<Employee>> getAll() {
        List<Employee> mockList = new ArrayList<>();

        try {
            String json = """
                [{
                    "id": 101,
                    "name": "John Doe",
                    "role": "USER",
                    "salary": 55000.0,
                    "phone": "9876543210",
                    "address": "123 Mock Street"
                }]
            """;
            mockList.add(objectMapper.readValue(json, Employee.class));
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(mockList);
    }

    // Update salary — mock response with new salary
    @PutMapping("/{id}/salary")
    @Operation(summary = "Update employee salary", description = "Updates the salary of an employee.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Salary updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid salary data"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<Employee> updateSalary(
            @PathVariable Long id,
            @RequestParam Double newSalary
    ) throws JsonProcessingException {
        log.info("Updating salary for employee {} to {}", id, newSalary);

        String json = """
            {
                "id": %d,
                "name": "Updated User",
                "role": "USER",
                "salary": %.2f,
                "phone": "9999999999",
                "address": "Updated Lane"
            }
        """.formatted(id, newSalary);

        Employee updated = objectMapper.readValue(json, Employee.class);
        return ResponseEntity.ok(updated);
    }

    // Delete employee — return 204 No Content
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an employee", description = "Deletes an employee by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        log.info("Mock delete employee with id {}", id);
        return ResponseEntity.noContent().build();
    }

    // Update contact details — mock contact update
    @PutMapping("/{id}/contact")
    @Operation(summary = "Update employee contact details", description = "Updates the contact details (phone, address) of an employee.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contact details updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid contact details"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<Employee> updateContactDetails(
            @PathVariable Long id,
            @RequestBody ContactUpdateRequest contactRequest
    ) throws JsonProcessingException {
        log.info("Updating contact for employee {} with data: {}", id, contactRequest);

        String json = """
            {
                "id": %d,
                "name": "Contact Updated User",
                "role": "USER",
                "salary": 60000.0,
                "phone": "%s",
                "address": "%s"
            }
        """.formatted(id, contactRequest.getPhone(), contactRequest.getAddress());

        Employee updated = objectMapper.readValue(json, Employee.class);
        return ResponseEntity.ok(updated);
    }
}
