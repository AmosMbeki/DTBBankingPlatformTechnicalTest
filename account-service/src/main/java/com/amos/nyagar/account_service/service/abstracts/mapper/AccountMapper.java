package com.amos.nyagar.account_service.service.abstracts.mapper;

import com.amos.nyagar.account_service.entity.Account;
import com.amos.nyagar.account_service.entity.DTO.AccountDTO;
import com.amos.nyagar.account_service.entity.request.AccountSaveRequest;
import com.amos.nyagar.account_service.entity.request.AccountUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AccountMapper {
    AccountDTO toDto(Account account);
    Account toEntity(AccountDTO accountDTO);
    
    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Account toEntityFromSaveRequest(AccountSaveRequest request);
    
    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    Account toEntityFromUpdateRequest(AccountUpdateRequest request);
}
