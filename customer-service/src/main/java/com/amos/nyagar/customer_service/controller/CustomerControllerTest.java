package com.amos.nyagar.customer_service.controller;
import com.amos.nyagar.customer_service.entity.DTO.CustomerDTO;
import com.amos.nyagar.customer_service.service.abstracts.CustomerService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    void createCustomer_ShouldReturnCreated() throws Exception {
        CustomerDTO customerDTO = createTestCustomerDTO();
        given(customerService.createCustomer(any(CustomerDTO.class))).willReturn(customerDTO);

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void getCustomerById_ShouldReturnCustomer() throws Exception {
        CustomerDTO customerDTO = createTestCustomerDTO();
        given(customerService.getCustomerById(toString())).willReturn(customerDTO);

        mockMvc.perform(get("/api/v1/customers/" + customerDTO.getCustomerId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(customerDTO.getCustomerId()));
    }

    @Test
    void searchCustomers_ShouldReturnPage() throws Exception {
        CustomerDTO customerDTO = createTestCustomerDTO();
        Page<CustomerDTO> page = new PageImpl<>(Collections.singletonList(customerDTO));
        given(customerService.searchCustomers(any(), any(), any(), any())).willReturn(page);

        mockMvc.perform(get("/api/v1/customers/search?name=John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName").value("John"));
    }

    private CustomerDTO createTestCustomerDTO() {
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerId(UUID.randomUUID().toString());
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }
}
