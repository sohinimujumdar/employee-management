package com.example.employee.controller;

import com.example.employee.dto.ContactUpdateRequest;
import com.example.employee.entity.Employee;
import com.example.employee.exception.UnauthorizedAccessException;
import com.example.employee.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Create new employee - Only Admin
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
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Employee employee) {
        try {
            if (employee == null) {
                return ResponseEntity.badRequest().build();

            }

            Employee createdEmployee = employeeService.createEmployee(employee);
            return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED); // 201

        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403
        }
    }

    // Get all users
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
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Employee> employees = employeeService.getEmployees();
            return ResponseEntity.ok(employees);
        } catch (IllegalArgumentException e) {
            //return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            return ResponseEntity.badRequest().build();
        }
    }


    // Update salary by ID — Admin or the employee can update their salary
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
    @PutMapping("/{id}/salary")
    public ResponseEntity<Employee> updateSalary(
            @PathVariable Long id,
            @RequestParam Double newSalary
    ) {
        try {
            Employee updatedEmployee = employeeService.updateSalary(id, newSalary);
            return ResponseEntity.ok(updatedEmployee); // 200 OK
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // 400
        }
    }

    // Delete employee — no access check
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
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build(); // Returns 204 No Content
    }


    // Update contact details — Admin or the employee can update their own details
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
    @PutMapping("/{id}/contact")
    public ResponseEntity<?> updateContactDetails(
            @PathVariable Long id,
            @RequestBody ContactUpdateRequest contactRequest
    ) {
        try {
            if (contactRequest == null || contactRequest.getPhone() == null || contactRequest.getAddress() == null) {
                return ResponseEntity.badRequest().build(); // 400
            }

            Employee updatedEmployee = employeeService.updateContactDetails(
                    id,
                    contactRequest.getPhone(),
                    contactRequest.getAddress()
            );
            return ResponseEntity.ok(updatedEmployee); // 200

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build(); // 400
        }
    }
}