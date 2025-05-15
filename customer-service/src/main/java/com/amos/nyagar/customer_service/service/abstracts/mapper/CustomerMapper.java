package com.amos.nyagar.customer_service.service.abstracts.mapper;

import com.amos.nyagar.customer_service.entity.Customer;
import com.amos.nyagar.customer_service.entity.DTO.CustomerDTO;
import com.amos.nyagar.customer_service.entity.request.CustomerSaveRequest;
import com.amos.nyagar.customer_service.entity.request.CustomerUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {
    CustomerDTO toDto(Customer customer);
    Customer toEntity(CustomerDTO customerDTO);

    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Customer toEntityFromSaveRequest(CustomerSaveRequest request);

    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Customer toEntityFromUpdateRequest(CustomerUpdateRequest request);
}
