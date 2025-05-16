package com.amos.nyagar.account_service.service.concrete;



import com.amos.nyagar.account_service.entity.Account;
import com.amos.nyagar.account_service.entity.DTO.AccountDTO;
import com.amos.nyagar.account_service.repository.AccountRepository;
import com.amos.nyagar.account_service.service.abstracts.mapper.AccountMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;
    private AccountDTO accountDTO;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setAccountId(UUID.randomUUID().toString());
        account.setIban("GB29NWBK60161331926819");
        account.setBicSwift("NWBKGB2L");
        account.setCustomerId("cust-12345");

        accountDTO = new AccountDTO();
        accountDTO.setAccountId(account.getAccountId());
        accountDTO.setIban(account.getIban());
        accountDTO.setBicSwift(account.getBicSwift());
        accountDTO.setCustomerId(account.getCustomerId());
    }

    @Test
    void createAccount_ShouldReturnCreatedAccount() {
        // Arrange
        when(accountMapper.toEntity(any(AccountDTO.class))).thenReturn(account);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toDto(any(Account.class))).thenReturn(accountDTO);
        when(accountRepository.existsByIban(anyString())).thenReturn(false);

        // Act
        AccountDTO result = accountService.createAccount(accountDTO);

        // Assert
        assertNotNull(result);
        assertEquals(accountDTO.getIban(), result.getIban());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void createAccount_ShouldThrowException_WhenIbanExists() {
        // Arrange
        when(accountRepository.existsByIban(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                accountService.createAccount(accountDTO));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void getAccountById_ShouldReturnAccount_WhenExists() {
        // Arrange
        when(accountRepository.findByAccountId(account.getAccountId()))
                .thenReturn(Optional.of(account));
        when(accountMapper.toDto(any(Account.class))).thenReturn(accountDTO);

        // Act
        AccountDTO result = accountService.getAccountById(account.getAccountId());

        // Assert
        assertNotNull(result);
        assertEquals(accountDTO.getAccountId(), result.getAccountId());
    }

    @Test
    void getAccountById_ShouldThrowException_WhenNotFound() {
        // Arrange
        when(accountRepository.findByAccountId(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                accountService.getAccountById("non-existent-id"));
    }

    @Test
    void getAllAccounts_ShouldReturnPaginatedResults() {
        // Arrange
        Page<Account> accountPage = new PageImpl<>(Collections.singletonList(account));
        when(accountRepository.findAll(any(Pageable.class))).thenReturn(accountPage);
        when(accountMapper.toDto(any(Account.class))).thenReturn(accountDTO);

        // Act
        Page<AccountDTO> result = accountService.getAllAccounts(Pageable.unpaged());

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(accountDTO.getIban(), result.getContent().get(0).getIban());
    }

    @Test
    void searchAccounts_ShouldReturnFilteredResults_ByIban() {
        // Arrange
        Page<Account> accountPage = new PageImpl<>(Collections.singletonList(account));
        when(accountRepository.searchAccounts(anyString(), isNull(), any(Pageable.class)))
                .thenReturn(accountPage);
        when(accountMapper.toDto(any(Account.class))).thenReturn(accountDTO);

        // Act
        Page<AccountDTO> result = accountService.searchAccounts("GB29", null, Pageable.unpaged());

        // Assert
        assertEquals(1, result.getTotalElements());
        verify(accountRepository, times(1))
                .searchAccounts(anyString(), isNull(), any(Pageable.class));
    }

    @Test
    void updateAccount_ShouldReturnUpdatedAccount() {
        // Arrange
        when(accountRepository.findByAccountId(account.getAccountId()))
                .thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toDto(any(Account.class))).thenReturn(accountDTO);

        // Act
        AccountDTO result = accountService.updateAccount(account.getAccountId(), accountDTO);

        // Assert
        assertNotNull(result);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void updateAccount_ShouldPreserveCustomerId() {
        // Arrange
        AccountDTO updateDTO = new AccountDTO();
        updateDTO.setIban("NEWIBAN123");
        updateDTO.setBicSwift("NEWBIC123");

        when(accountRepository.findByAccountId(account.getAccountId()))
                .thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toDto(any(Account.class))).thenReturn(accountDTO);

        // Act
        accountService.updateAccount(account.getAccountId(), updateDTO);

        // Assert
        verify(accountMapper).toEntity(updateDTO);
        assertNotNull(account.getCustomerId()); // Ensure customerId wasn't nulled
    }

    @Test
    void deleteAccount_ShouldDeleteAccount() {
        // Arrange
        when(accountRepository.findByAccountId(account.getAccountId()))
                .thenReturn(Optional.of(account));
        doNothing().when(accountRepository).delete(any(Account.class));

        // Act
        accountService.deleteAccount(account.getAccountId());

        // Assert
        verify(accountRepository, times(1)).delete(any(Account.class));
    }

    @Test
    void getAccountsByCustomerId_ShouldReturnCustomerAccounts() {
        // Arrange
        Page<Account> accountPage = new PageImpl<>(Collections.singletonList(account));
        when(accountRepository.findByCustomerId(anyString(), any(Pageable.class)))
                .thenReturn(accountPage);
        when(accountMapper.toDto(any(Account.class))).thenReturn(accountDTO);

        // Act
        Page<AccountDTO> result = accountService.getAccountsByCustomerId("cust-12345", Pageable.unpaged());

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("cust-12345", result.getContent().get(0).getCustomerId());
    }
}