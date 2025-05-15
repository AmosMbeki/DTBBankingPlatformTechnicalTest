package com.amos.nyagar.customer_service.controller;

import com.amos.nyagar.customer_service.service.abstracts.CustomerService;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.amos.nyagar.customer_service.entity.DTO.CustomerDTO;
import com.amos.nyagar.customer_service.entity.request.CustomerSaveRequest;
import com.amos.nyagar.customer_service.entity.request.CustomerUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/customers")
@Tag(name = "Customers Controller", description = "Controller for Customer Service management")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerSaveRequest request) {
        CustomerDTO customerDTO = convertSaveRequestToDto(request);
        CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable String customerId) {
        CustomerDTO customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(customer);
    }

    @GetMapping
    public ResponseEntity<Page<CustomerDTO>> getAllCustomers(
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        Page<CustomerDTO> customers = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CustomerDTO>> searchCustomers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @PageableDefault(size = 20) Pageable pageable) {

        Page<CustomerDTO> customers = customerService.searchCustomers(
                name, startDate, endDate, pageable);
        return ResponseEntity.ok(customers);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable String customerId,
            @Valid @RequestBody CustomerUpdateRequest request) {

        CustomerDTO customerDTO = convertUpdateRequestToDto(request);
        CustomerDTO updatedCustomer = customerService.updateCustomer(customerId, customerDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

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