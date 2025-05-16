package com.amos.nyagar.account_service.service.abstracts;

import com.amos.nyagar.account_service.entity.DTO.AccountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {
    AccountDTO createAccount(AccountDTO accountDTO);
    AccountDTO getAccountById(String accountId);
    Page<AccountDTO> getAllAccounts(Pageable pageable);
    Page<AccountDTO> searchAccounts(String iban, String bicSwift, Pageable pageable);
    AccountDTO updateAccount(String accountId, AccountDTO accountDTO);
    void deleteAccount(String accountId);
    Page<AccountDTO> getAccountsByCustomerId(String customerId, Pageable pageable);
}