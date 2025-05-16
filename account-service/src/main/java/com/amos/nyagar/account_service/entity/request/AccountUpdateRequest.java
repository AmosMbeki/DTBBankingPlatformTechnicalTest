package com.amos.nyagar.account_service.entity.request;

import jakarta.validation.constraints.Size;

public class AccountUpdateRequest {
    @Size(min = 15, max = 34, message = "IBAN must be between 15 and 34 characters")
    private String iban;

    @Size(min = 8, max = 11, message = "BIC/SWIFT must be 8 or 11 characters")
    private String bicSwift;

    // Constructors
    public AccountUpdateRequest() {
    }

    public AccountUpdateRequest(String iban, String bicSwift) {
        this.iban = iban;
        this.bicSwift = bicSwift;
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
}