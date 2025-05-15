package com.amos.nyagar.customer_service.entity.DTO;

import java.time.LocalDateTime;

public class CustomerDTO {
    private String customerId;
    private String firstName;
    private String lastName;
    private String otherName;
    private LocalDateTime createdAt;

    // Constructors
    public CustomerDTO() {
    }

    public CustomerDTO(String customerId, String firstName, String lastName, String otherName, LocalDateTime createdAt) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.otherName = otherName;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // getFullName helper method
    public String getFullName() {
        if (otherName == null || otherName.isEmpty()) {
            return firstName + " " + lastName;
        }
        return firstName + " " + otherName + " " + lastName;
    }
}
