package com.amos.nyagar.account_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Data
public class Account {
    @Id
    @Column(name = "account_id")
    private String accountId;
    
    @Column(name = "iban", nullable = false, unique = true, length = 34)
    private String iban;
    
    @Column(name = "bic_swift", nullable = false, length = 11)
    private String bicSwift;
    
    @Column(name = "customer_id", nullable = false)
    private String customerId;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}