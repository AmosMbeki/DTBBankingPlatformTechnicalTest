package com.amos.nyagar.card_service.repository;

import com.amos.nyagar.card_service.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {
    
    // Find by card ID
    Optional<Card> findByCardId(String cardId);
    
    // Find all cards for an account
    Page<Card> findByAccountId(String accountId, Pageable pageable);
    
    // Find by card type (VIRTUAL/PHYSICAL) for an account
    Optional<Card> findByAccountIdAndCardType(String accountId, String cardType);
    
    // Search by card alias (partial match)
    @Query("SELECT c FROM Card c WHERE c.cardAlias LIKE %:alias%")
    Page<Card> findByCardAliasContaining(@Param("alias") String alias, Pageable pageable);
    
    // Search by card type
    Page<Card> findByCardType(String cardType, Pageable pageable);
    
    // Search by PAN (exact match, for internal use)
    Optional<Card> findByPan(String pan);
    
    // Combined search with filters
    @Query("SELECT c FROM Card c WHERE " +
           "(c.cardAlias LIKE %:alias% OR :alias IS NULL) AND " +
           "(c.cardType = :cardType OR :cardType IS NULL) AND " +
           "(c.accountId = :accountId OR :accountId IS NULL)")
    Page<Card> searchCards(
            @Param("alias") String cardAlias,
            @Param("cardType") String cardType,
            @Param("accountId") String accountId,
            Pageable pageable);
    
    // Count cards by account and type (for business rule enforcement)
    int countByAccountIdAndCardType(String accountId, String cardType);
    
    // Check if card exists for account (for validation)
    boolean existsByCardIdAndAccountId(String cardId, String accountId);
}