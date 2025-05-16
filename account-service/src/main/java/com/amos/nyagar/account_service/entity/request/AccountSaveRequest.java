package com.amos.nyagar.account_service.entity.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AccountSaveRequest {
    @NotBlank(message = "IBAN is mandatory")
    @Size(min = 15, max = 34, message = "IBAN must be between 15 and 34 characters")
    private String iban;

    @NotBlank(message = "BIC/SWIFT is mandatory")
    @Size(min = 8, max = 11, message = "BIC/SWIFT must be 8 or 11 characters")
    private String bicSwift;

    @NotBlank(message = "Customer ID is mandatory")
    private String customerId;

    // Constructors
    public AccountSaveRequest() {
    }

    public AccountSaveRequest(String iban, String bicSwift, String customerId) {
        this.iban = iban;
        this.bicSwift = bicSwift;
        this.customerId = customerId;
    }

    // Getters and Setters
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
}