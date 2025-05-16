package com.amos.nyagar.card_service.entity.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CardSaveRequest {
    @Size(max = 100, message = "Card alias cannot exceed 100 characters")
    private String cardAlias;

    @NotBlank(message = "Account ID is mandatory")
    private String accountId;

    @NotBlank(message = "Card type is mandatory")
    @Pattern(regexp = "VIRTUAL|PHYSICAL", message = "Card type must be VIRTUAL or PHYSICAL")
    private String cardType;

    // Constructors
    public CardSaveRequest() {
    }

    public CardSaveRequest(String cardAlias, String accountId, String cardType) {
        this.cardAlias = cardAlias;
        this.accountId = accountId;
        this.cardType = cardType;
    }

    // Getters and Setters
    public String getCardAlias() {
        return cardAlias;
    }

    public void setCardAlias(String cardAlias) {
        this.cardAlias = cardAlias;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}