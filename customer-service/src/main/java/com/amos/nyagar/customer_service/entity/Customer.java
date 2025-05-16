package com.amos.nyagar.customer_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Data
public class Customer {
    
    @Id
    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "other_name", length = 100)
    private String otherName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Pre-persist hook to set creation timestamp
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}