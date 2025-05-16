package com.amos.nyagar.card_service.service.abstracts;

import com.amos.nyagar.card_service.entity.DTO.CardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardService {
    CardDTO createCard(CardDTO cardDTO);
    CardDTO getCardById(String cardId, boolean showSensitiveDetails);
    Page<CardDTO> getAllCards(Pageable pageable);
    Page<CardDTO> searchCards(String cardAlias, String cardType, String accountId, Pageable pageable);
    CardDTO updateCardAlias(String cardId, CardDTO cardDTO);
    void deleteCard(String cardId);
    Page<CardDTO> getCardsByAccountId(String accountId, Pageable pageable);
    boolean accountHasCardType(String accountId, String cardType);
}
