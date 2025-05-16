package com.amos.nyagar.customer_service;

import com.amos.nyagar.customer_service.entity.Customer;
import com.amos.nyagar.customer_service.entity.DTO.CustomerDTO;
import com.amos.nyagar.customer_service.repository.CustomerRepository;
import com.amos.nyagar.customer_service.service.abstracts.mapper.CustomerMapper;
import com.amos.nyagar.customer_service.service.concrete.CustomerServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setCustomerId(UUID.randomUUID().toString());
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setCreatedAt(LocalDateTime.now());

        customerDTO = new CustomerDTO();
        customerDTO.setCustomerId(customer.getCustomerId());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setCreatedAt(customer.getCreatedAt());
    }

    @Test
    void createCustomer_ShouldReturnCreatedCustomer() {
        // Arrange
        when(customerMapper.toEntity(any(CustomerDTO.class))).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toDto(any(Customer.class))).thenReturn(customerDTO);

        // Act
        CustomerDTO result = customerService.createCustomer(customerDTO);

        // Assert
        assertNotNull(result);
        assertEquals(customerDTO.getCustomerId(), result.getCustomerId());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void getCustomerById_ShouldReturnCustomer_WhenExists() {
        // Arrange
        when(customerRepository.findByCustomerId(customer.getCustomerId()))
                .thenReturn(Optional.of(customer));
        when(customerMapper.toDto(any(Customer.class))).thenReturn(customerDTO);

        // Act
        CustomerDTO result = customerService.getCustomerById(customer.getCustomerId());

        // Assert
        assertNotNull(result);
        assertEquals(customerDTO.getCustomerId(), result.getCustomerId());
    }

    @Test
    void getCustomerById_ShouldThrowException_WhenNotFound() {
        // Arrange
        when(customerRepository.findByCustomerId(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> 
            customerService.getCustomerById("non-existent-id"));
    }

    @Test
    void getAllCustomers_ShouldReturnPaginatedResults() {
        // Arrange
        Page<Customer> customerPage = new PageImpl<>(Collections.singletonList(customer));
        when(customerRepository.findAll(any(Pageable.class))).thenReturn(customerPage);
        when(customerMapper.toDto(any(Customer.class))).thenReturn(customerDTO);

        // Act
        Page<CustomerDTO> result = customerService.getAllCustomers(Pageable.unpaged());

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(customerDTO.getCustomerId(), result.getContent().get(0).getCustomerId());
    }

    @Test
    void searchCustomers_ShouldReturnFilteredResults_ByName() {
        // Arrange
        Page<Customer> customerPage = new PageImpl<>(Collections.singletonList(customer));
        when(customerRepository.findByFullNameContaining(anyString(), any(Pageable.class)))
                .thenReturn(customerPage);
        when(customerMapper.toDto(any(Customer.class))).thenReturn(customerDTO);

        // Act
        Page<CustomerDTO> result = customerService.searchCustomers("John", null, null, Pageable.unpaged());

        // Assert
        assertEquals(1, result.getTotalElements());
        verify(customerRepository, times(1)).findByFullNameContaining(anyString(), any(Pageable.class));
    }

    @Test
    void updateCustomer_ShouldReturnUpdatedCustomer() {
        // Arrange
        when(customerRepository.findByCustomerId(customer.getCustomerId()))
                .thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toDto(any(Customer.class))).thenReturn(customerDTO);

        // Act
        CustomerDTO result = customerService.updateCustomer(customer.getCustomerId(), customerDTO);

        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void deleteCustomer_ShouldDeleteCustomer() {
        // Arrange
        when(customerRepository.findByCustomerId(customer.getCustomerId()))
                .thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).delete(any(Customer.class));

        // Act
        customerService.deleteCustomer(customer.getCustomerId());

        // Assert
        verify(customerRepository, times(1)).delete(any(Customer.class));
    }
}
