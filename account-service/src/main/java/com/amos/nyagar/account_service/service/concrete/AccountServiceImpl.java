package com.amos.nyagar.account_service.service.concrete;


import com.amos.nyagar.account_service.entity.Account;
import com.amos.nyagar.account_service.entity.DTO.AccountDTO;
import com.amos.nyagar.account_service.repository.AccountRepository;
import com.amos.nyagar.account_service.service.abstracts.AccountService;
import com.amos.nyagar.account_service.service.abstracts.mapper.AccountMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository, 
                            AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public AccountDTO createAccount(AccountDTO accountDTO) {
        if (accountRepository.existsByIban(accountDTO.getIban())) {
            throw new IllegalArgumentException("Account with this IBAN already exists");
        }

        Account account = accountMapper.toEntity(accountDTO);
        account.setAccountId(UUID.randomUUID().toString());
        Account savedAccount = accountRepository.save(account);
        return accountMapper.toDto(savedAccount);
    }

    @Override
    public AccountDTO getAccountById(String accountId) {
        return accountRepository.findByAccountId(accountId)
                .map(accountMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Override
    public Page<AccountDTO> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable)
                .map(accountMapper::toDto);
    }

    @Override
    public Page<AccountDTO> searchAccounts(String iban, String bicSwift, Pageable pageable) {
        return accountRepository.searchAccounts(iban, bicSwift, pageable)
                .map(accountMapper::toDto);
    }

    @Override
    public AccountDTO updateAccount(String accountId, AccountDTO accountDTO) {
        Account existingAccount = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Prevent updating customerId
        accountDTO.setCustomerId(existingAccount.getCustomerId());
        
        accountMapper.toEntity(accountDTO);
        Account updatedAccount = accountRepository.save(existingAccount);
        return accountMapper.toDto(updatedAccount);
    }

    @Override
    public void deleteAccount(String accountId) {
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        accountRepository.delete(account);
    }

    @Override
    public Page<AccountDTO> getAccountsByCustomerId(String customerId, Pageable pageable) {
        return accountRepository.findByCustomerId(customerId, pageable)
                .map(accountMapper::toDto);
    }
}

