package com.amos.nyagar.customer_service.service.concrete;

import com.amos.nyagar.customer_service.service.abstracts.CustomerService;

import com.amos.nyagar.customer_service.service.abstracts.mapper.CustomerMapper;
import com.amos.nyagar.customer_service.entity.Customer;
import com.amos.nyagar.customer_service.entity.DTO.CustomerDTO;
import com.amos.nyagar.customer_service.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.toEntity(customerDTO);
        customer.setCustomerId(UUID.randomUUID().toString());
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    @Override
    public CustomerDTO getCustomerById(String customerId) {
        return customerRepository.findByCustomerId(customerId)
                .map(customerMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public Page<CustomerDTO> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(customerMapper::toDto);
    }

    @Override
    public Page<CustomerDTO> searchCustomers(String name, LocalDateTime startDate,
                                             LocalDateTime endDate, Pageable pageable) {
        if (name != null && startDate != null && endDate != null) {
            return customerRepository.findByFullNameAndCreatedAtBetween(name, startDate, endDate, pageable)
                    .map(customerMapper::toDto);
        } else if (name != null) {
            return customerRepository.findByFullNameContaining(name, pageable)
                    .map(customerMapper::toDto);
        } else if (startDate != null && endDate != null) {
            return customerRepository.findByCreatedAtBetween(startDate, endDate, pageable)
                    .map(customerMapper::toDto);
        }
        return getAllCustomers(pageable);
    }

    @Override
    public CustomerDTO updateCustomer(String customerId, CustomerDTO customerDTO) {
        Customer existingCustomer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customerMapper.toEntity(customerDTO);
        // customerMapper.toEntity(customerDTO, existingCustomer);
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return customerMapper.toDto(updatedCustomer);
    }

    @Override
    public void deleteCustomer(String customerId) {
        Customer customer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customerRepository.delete(customer);
    }
}
