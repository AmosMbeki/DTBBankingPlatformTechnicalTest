package com.amos.nyagar.card_service.entity.request;

import jakarta.validation.constraints.Size;

public class CardUpdateRequest {
    @Size(max = 100, message = "Card alias cannot exceed 100 characters")
    private String cardAlias;

    // Constructors
    public CardUpdateRequest() {
    }

    public CardUpdateRequest(String cardAlias) {
        this.cardAlias = cardAlias;
    }

    // Getters and Setters
    public String getCardAlias() {
        return cardAlias;
    }

    public void setCardAlias(String cardAlias) {
        this.cardAlias = cardAlias;
    }
}