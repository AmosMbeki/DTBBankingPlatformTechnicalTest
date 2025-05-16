package com.amos.nyagar.account_service.entity.DTO;

import java.time.LocalDateTime;

public class AccountDTO {
    private String accountId;
    private String iban;
    private String bicSwift;
    private String customerId;
    private LocalDateTime createdAt;

    // Constructors
    public AccountDTO() {
    }

    public AccountDTO(String accountId, String iban, String bicSwift, String customerId, LocalDateTime createdAt) {
        this.accountId = accountId;
        this.iban = iban;
        this.bicSwift = bicSwift;
        this.customerId = customerId;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBicSwift() {
        return bicSwift;
    }

    public void setBicSwift(String bicSwift) {
        this.bicSwift = bicSwift;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Validation methods
    public boolean isValidIban() {
        // Basic IBAN validation - should be implemented properly
        return iban != null && iban.length() >= 15 && iban.length() <= 34;
    }

    public boolean isValidBicSwift() {
        // Basic BIC/SWIFT validation - should be implemented properly
        return bicSwift != null && (bicSwift.length() == 8 || bicSwift.length() == 11);
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "accountId='" + accountId + '\'' +
                ", iban='" + iban + '\'' +
                ", bicSwift='" + bicSwift + '\'' +
                ", customerId='" + customerId + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
