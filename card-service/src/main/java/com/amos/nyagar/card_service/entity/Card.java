package com.amos.nyagar.card_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cards")
@Data
public class Card {

    @Id
    @Column(name = "card_id")
    private String cardId;
    
    @Column(name = "card_alias", length = 100)
    private String cardAlias;
    
    @Column(name = "account_id", nullable = false)
    private String accountId;
    
    @Column(name = "card_type", nullable = false, length = 10)
    private String cardType; // VIRTUAL or PHYSICAL
    
    @Column(name = "pan", nullable = false, length = 16)
    private String pan; // Primary Account Number
    
    @Column(name = "cvv", nullable = false, length = 3)
    private String cvv;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    // Business logic validation
    public boolean isValidCardType() {
        return "VIRTUAL".equalsIgnoreCase(cardType) || 
               "PHYSICAL".equalsIgnoreCase(cardType);
    }
    
    // Pre-persist hook to generate IDs if needed
    @PrePersist
    public void generateIdsIfNeeded() {
        if (this.cardId == null) {
            this.cardId = "card-" + UUID.randomUUID().toString();
        }
    }
}
