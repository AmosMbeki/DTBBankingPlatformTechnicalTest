package com.amos.nyagar.customer_service.repository;

import com.amos.nyagar.customer_service.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    // Find by exact customer ID
    Optional<Customer> findByCustomerId(String customerId);

    // Full text search by name with pagination
    @Query("SELECT c FROM Customer c WHERE " +
            "CONCAT(c.firstName, ' ', COALESCE(c.otherName, ''), ' ', c.lastName) LIKE %:name%")
    Page<Customer> findByFullNameContaining(@Param("name") String name, Pageable pageable);

    // Search by creation date range with pagination
    Page<Customer> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // Combined search (name and date range) with pagination
    @Query("SELECT c FROM Customer c WHERE " +
            "(CONCAT(c.firstName, ' ', COALESCE(c.otherName, ''), ' ', c.lastName) LIKE %:name%) AND " +
            "(c.createdAt BETWEEN :startDate AND :endDate)")
    Page<Customer> findByFullNameAndCreatedAtBetween(
            @Param("name") String name,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    // Check if customer exists by first and last name (for validation)
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
}
