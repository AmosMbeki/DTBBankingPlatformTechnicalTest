package com.amos.nyagar.account_service.repository;

import com.amos.nyagar.account_service.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    // Basic CRUD operations are inherited from JpaRepository

    // Find by exact account ID
    Optional<Account> findByAccountId(String accountId);

    // Find by IBAN (exact match)
    Optional<Account> findByIban(String iban);

    // Find by BIC/SWIFT (exact match)
    List<Account> findByBicSwift(String bicSwift);

    // Find all accounts for a specific customer
    Page<Account> findByCustomerId(String customerId, Pageable pageable);

    // Search by IBAN (partial match) with pagination
    @Query("SELECT a FROM Account a WHERE a.iban LIKE %:iban%")
    Page<Account> findByIbanContaining(@Param("iban") String iban, Pageable pageable);

    // Search by BIC/SWIFT (partial match) with pagination
    @Query("SELECT a FROM Account a WHERE a.bicSwift LIKE %:bicSwift%")
    Page<Account> findByBicSwiftContaining(@Param("bicSwift") String bicSwift, Pageable pageable);

    // Combined search (IBAN and BIC/SWIFT) with pagination
    @Query("SELECT a FROM Account a WHERE " +
            "(a.iban LIKE %:iban% OR :iban IS NULL) AND " +
            "(a.bicSwift LIKE %:bicSwift% OR :bicSwift IS NULL)")
    Page<Account> searchAccounts(
            @Param("iban") String iban,
            @Param("bicSwift") String bicSwift,
            Pageable pageable);

    // Check if IBAN already exists (for validation)
    boolean existsByIban(String iban);

    // Check if account exists for customer (for validation)
    boolean existsByAccountIdAndCustomerId(String accountId, String customerId);
}