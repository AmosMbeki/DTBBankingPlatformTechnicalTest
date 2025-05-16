package com.amos.nyagar.customer_service.controller;


import com.amos.nyagar.customer_service.entity.DTO.CustomerDTO;
import com.amos.nyagar.customer_service.entity.request.CustomerSaveRequest;
import com.amos.nyagar.customer_service.entity.request.CustomerUpdateRequest;
import com.amos.nyagar.customer_service.service.abstracts.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/customers")
@Tag(name = "Customer Management", description = "Endpoints for managing customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Create a new customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Customer created successfully",
                    content = @Content(schema = @Schema(implementation = CustomerDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "409", description = "Customer already exists")
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDTO> createCustomer(
            @Parameter(description = "Customer data to create") 
            @Valid @RequestBody CustomerSaveRequest request) {
        CustomerDTO customerDTO = convertSaveRequestToDto(request);
        CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    @Operation(summary = "Get customer by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer found",
                    content = @Content(schema = @Schema(implementation = CustomerDTO.class))),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping(value = "/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDTO> getCustomerById(
            @Parameter(description = "ID of the customer to be retrieved")
            @PathVariable String customerId) {
        CustomerDTO customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(customer);
    }

    @Operation(summary = "Get all customers with pagination")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved page of customers",
                content = @Content(schema = @Schema(implementation = Page.class)))
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CustomerDTO>> getAllCustomers(
            @ParameterObject @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        Page<CustomerDTO> customers = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(customers);
    }

    @Operation(summary = "Search customers with filters")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered customers",
                content = @Content(schema = @Schema(implementation = Page.class)))
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CustomerDTO>> searchCustomers(
            @Parameter(description = "Full text search on customer names") 
            @RequestParam(required = false) String name,
            
            @Parameter(description = "Start date for creation date range filter (ISO format)") 
            @RequestParam(required = false) LocalDateTime startDate,
            
            @Parameter(description = "End date for creation date range filter (ISO format)") 
            @RequestParam(required = false) LocalDateTime endDate,
            
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        
        Page<CustomerDTO> customers = customerService.searchCustomers(name, startDate, endDate, pageable);
        return ResponseEntity.ok(customers);
    }

    @Operation(summary = "Update customer details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer updated successfully",
                    content = @Content(schema = @Schema(implementation = CustomerDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PutMapping(value = "/{customerId}", 
               produces = MediaType.APPLICATION_JSON_VALUE, 
               consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDTO> updateCustomer(
            @Parameter(description = "ID of the customer to be updated")
            @PathVariable String customerId,
            
            @Parameter(description = "Updated customer data") 
            @Valid @RequestBody CustomerUpdateRequest request) {
        
        CustomerDTO customerDTO = convertUpdateRequestToDto(request);
        CustomerDTO updatedCustomer = customerService.updateCustomer(customerId, customerDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @Operation(summary = "Delete a customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "ID of the customer to be deleted")
            @PathVariable String customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

    // Helper methods
    private CustomerDTO convertSaveRequestToDto(CustomerSaveRequest request) {
        CustomerDTO dto = new CustomerDTO();
        dto.setFirstName(request.getFirstName());
        dto.setLastName(request.getLastName());
        dto.setOtherName(request.getOtherName());
        return dto;
    }

    private CustomerDTO convertUpdateRequestToDto(CustomerUpdateRequest request) {
        CustomerDTO dto = new CustomerDTO();
        dto.setFirstName(request.getFirstName());
        dto.setLastName(request.getLastName());
        dto.setOtherName(request.getOtherName());
        return dto;
    }
}