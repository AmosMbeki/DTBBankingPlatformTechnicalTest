package com.amos.nyagar.card_service.entity.DTO;

import java.time.LocalDateTime;

public class CardDTO {
    private String cardId;
    private String cardAlias;
    private String accountId;
    private String cardType;  // "VIRTUAL" or "PHYSICAL"
    private String pan;       // Primary Account Number
    private String cvv;
    private LocalDateTime createdAt;
    private boolean showSensitiveDetails; // For masking control

    // Constructors
    public CardDTO() {
    }

    public CardDTO(String cardId, String cardAlias, String accountId,
                   String cardType, String pan, String cvv,
                   LocalDateTime createdAt) {
        this.cardId = cardId;
        this.cardAlias = cardAlias;
        this.accountId = accountId;
        this.cardType = cardType;
        this.pan = pan;
        this.cvv = cvv;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

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

    public String getPan() {
        return showSensitiveDetails ? pan : maskPan(pan);
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getCvv() {
        return showSensitiveDetails ? cvv : "***";
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isShowSensitiveDetails() {
        return showSensitiveDetails;
    }

    public void setShowSensitiveDetails(boolean showSensitiveDetails) {
        this.showSensitiveDetails = showSensitiveDetails;
    }

    // Helper methods
    private String maskPan(String pan) {
        if (pan == null || pan.length() < 8) {
            return pan;
        }
        return pan.substring(0, 4) + "********" + pan.substring(pan.length() - 4);
    }

    // Business validation
    public boolean isValidCardType() {
        return "VIRTUAL".equalsIgnoreCase(cardType) ||
                "PHYSICAL".equalsIgnoreCase(cardType);
    }

    @Override
    public String toString() {
        return "CardDTO{" +
                "cardId='" + cardId + '\'' +
                ", cardAlias='" + cardAlias + '\'' +
                ", accountId='" + accountId + '\'' +
                ", cardType='" + cardType + '\'' +
                ", pan='" + getPan() + '\'' +  // Masked in toString
                ", cvv='" + getCvv() + '\'' +  // Masked in toString
                ", createdAt=" + createdAt +
                '}';
    }
}