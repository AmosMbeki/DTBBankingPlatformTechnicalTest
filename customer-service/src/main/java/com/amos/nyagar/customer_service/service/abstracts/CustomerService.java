package com.amos.nyagar.customer_service.service.abstracts;

import com.amos.nyagar.customer_service.entity.DTO.CustomerDTO;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface CustomerService {
    CustomerDTO createCustomer(CustomerDTO customerDTO);
    CustomerDTO getCustomerById(String customerId);
    Page<CustomerDTO> getAllCustomers(Pageable pageable);
    Page<CustomerDTO> searchCustomers(String name, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    CustomerDTO updateCustomer(String customerId, CustomerDTO customerDTO);
    void deleteCustomer(String customerId);
}
