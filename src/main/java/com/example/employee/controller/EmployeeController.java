package com.example.employee.controller;

import com.example.employee.dto.ContactUpdateRequest;
import com.example.employee.entity.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
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
    @Operation(
            summary = "Create a new employee",
            description = "Allows only ADMINs to create a new employee.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Employee created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Employee.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "id": 1,
                                                "name": "Alice Johnson",
                                                "role": "EMPLOYEE",
                                                "salary": 60000
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid employee input",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = "{\"message\": \"Invalid employee data\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden - Only ADMIN can create employees",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = "{\"message\": \"Access denied\"}")
                            )
                    ),
            }
    )
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
    @Operation(
            summary = "Get all employees",
            description = "Retrieves a list of all registered employees.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of employees",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Employee.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = "{\"message\": \"Invalid request parameters\"}")
                            )
                    )
            }
    )
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
    @Operation(
            summary = "Update employee salary",
            description = "Allows ADMIN or the employee to update the employee's salary.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Salary updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Employee.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "id": 1,
                                                "name": "John Doe",
                                                "role": "EMPLOYEE",
                                                "salary": 85000
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid salary input",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = "{\"message\": \"Invalid salary amount\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden - You are not allowed to update this salary",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = "{\"message\": \"Access denied\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Employee not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = "{\"message\": \"Employee not found\"}")
                            )
                    )
            }
    )
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
    @Operation(
            summary = "Delete an employee by ID",
            description = "Deletes an employee with the specified ID. Returns 204 if successful, or 404 if not found.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Employee deleted successfully (No Content returned)",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(type = "string"),
                                    examples = @ExampleObject(value = "\"Emplooyee Deleted successfully\"")
                            )

                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Employee not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = "{\"message\": \"Employee not found\"}")
                            )
                    )
            }
    )
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        log.info("Mock delete employee with id {}", id);
        return ResponseEntity.noContent().build();
    }

    // Update contact details — mock contact update
    @PutMapping("/{id}/contact")

    @Operation(
            summary = "Update employee contact details",
            description = "Allows ADMIN or the employee themselves to update contact information such as phone and address.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Contact details updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Employee.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "id": 1,
                                                "name": "Jane Smith",
                                                "role": "EMPLOYEE",
                                                "phone": "123-456-7890",
                                                "address": "123 Main Street"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid contact details input",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = "{\"message\": \"Invalid contact details\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden - Not allowed to update this employee's contact details",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = "{\"message\": \"Access denied\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Employee not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = "{\"message\": \"Employee not found\"}")
                            )
                    )
            }
    )
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
