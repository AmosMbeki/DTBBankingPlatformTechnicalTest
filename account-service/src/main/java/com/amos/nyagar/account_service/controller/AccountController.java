package com.amos.nyagar.account_service.controller;


import com.amos.nyagar.account_service.entity.DTO.AccountDTO;
import com.amos.nyagar.account_service.entity.request.AccountSaveRequest;
import com.amos.nyagar.account_service.entity.request.AccountUpdateRequest;
import com.amos.nyagar.account_service.service.abstracts.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@Tag(name = "Account Management", description = "Endpoints for managing bank accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(
        summary = "Create a new account",
        description = "Creates a new bank account with the provided details. IBAN must be unique."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Account created successfully",
                    content = @Content(schema = @Schema(implementation = AccountDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Account with this IBAN already exists")
    })
    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AccountDTO> createAccount(
            @Parameter(description = "Account creation data", required = true)
            @Valid @RequestBody AccountSaveRequest request) {
        AccountDTO accountDTO = convertToAccountDTO(request);
        AccountDTO createdAccount = accountService.createAccount(accountDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @Operation(
        summary = "Get account by ID",
        description = "Retrieves account details for the specified account ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Account found",
                    content = @Content(schema = @Schema(implementation = AccountDTO.class))),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping(value = "/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDTO> getAccountById(
            @Parameter(description = "ID of the account to retrieve", required = true)
            @PathVariable String accountId) {
        AccountDTO account = accountService.getAccountById(accountId);
        return ResponseEntity.ok(account);
    }

    @Operation(
        summary = "Get all accounts",
        description = "Retrieves all accounts with pagination support"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Successfully retrieved accounts",
        content = @Content(schema = @Schema(implementation = Page.class))
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AccountDTO>> getAllAccounts(
            @ParameterObject
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        Page<AccountDTO> accounts = accountService.getAllAccounts(pageable);
        return ResponseEntity.ok(accounts);
    }

    @Operation(
        summary = "Search accounts",
        description = "Search accounts with filters for IBAN and BIC/SWIFT"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved filtered accounts",
        content = @Content(schema = @Schema(implementation = Page.class))
    )
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AccountDTO>> searchAccounts(
            @Parameter(description = "Partial or complete IBAN to search for")
            @RequestParam(required = false) String iban,
            
            @Parameter(description = "Partial or complete BIC/SWIFT to search for")
            @RequestParam(required = false) String bicSwift,
            
            @ParameterObject
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<AccountDTO> accounts = accountService.searchAccounts(iban, bicSwift, pageable);
        return ResponseEntity.ok(accounts);
    }

    @Operation(
        summary = "Update account details",
        description = "Updates the specified account's details. Note: customerId cannot be changed."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Account updated successfully",
                    content = @Content(schema = @Schema(implementation = AccountDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PutMapping(
        value = "/{accountId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AccountDTO> updateAccount(
            @Parameter(description = "ID of the account to update", required = true)
            @PathVariable String accountId,
            
            @Parameter(description = "Updated account data", required = true)
            @Valid @RequestBody AccountUpdateRequest request) {
        
        AccountDTO accountDTO = convertToAccountDTO(request);
        AccountDTO updatedAccount = accountService.updateAccount(accountId, accountDTO);
        return ResponseEntity.ok(updatedAccount);
    }

    @Operation(
        summary = "Delete an account",
        description = "Deletes the specified account"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(
            @Parameter(description = "ID of the account to delete", required = true)
            @PathVariable String accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Get accounts by customer",
        description = "Retrieves all accounts belonging to a specific customer"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved customer accounts",
        content = @Content(schema = @Schema(implementation = Page.class))
    )
    @GetMapping(value = "/customer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AccountDTO>> getAccountsByCustomerId(
            @Parameter(description = "ID of the customer", required = true)
            @PathVariable String customerId,
            
            @ParameterObject
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<AccountDTO> accounts = accountService.getAccountsByCustomerId(customerId, pageable);
        return ResponseEntity.ok(accounts);
    }

    private AccountDTO convertToAccountDTO(AccountSaveRequest request) {
        AccountDTO dto = new AccountDTO();
        dto.setIban(request.getIban());
        dto.setBicSwift(request.getBicSwift());
        dto.setCustomerId(request.getCustomerId());
        return dto;
    }

    private AccountDTO convertToAccountDTO(AccountUpdateRequest request) {
        AccountDTO dto = new AccountDTO();
        dto.setIban(request.getIban());
        dto.setBicSwift(request.getBicSwift());
        return dto;
    }
}