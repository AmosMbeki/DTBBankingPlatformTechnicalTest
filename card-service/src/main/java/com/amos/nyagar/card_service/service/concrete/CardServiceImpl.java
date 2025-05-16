package com.amos.nyagar.card_service.service.concrete;

import com.amos.nyagar.card_service.service.abstracts.CardService;
import com.amos.nyagar.card_service.service.abstracts.mapper.CardMapper;
import com.amos.nyagar.card_service.entity.Card;
import com.amos.nyagar.card_service.entity.DTO.CardDTO;
import com.amos.nyagar.card_service.repository.CardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    public CardServiceImpl(CardRepository cardRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }

    @Override
    public CardDTO createCard(CardDTO cardDTO) {
        // Validate card type
        if (!cardDTO.isValidCardType()) {
            throw new IllegalArgumentException("Invalid card type. Must be VIRTUAL or PHYSICAL");
        }

        // Check card limits
        if (accountHasCardType(cardDTO.getAccountId(), cardDTO.getCardType())) {
            throw new IllegalStateException("Account already has a " + cardDTO.getCardType() + " card");
        }

        Card card = cardMapper.toEntity(cardDTO);
        card.setCardId("card-" + UUID.randomUUID().toString());
        card.setPan(generateSecurePan());
        card.setCvv(generateSecureCvv());

        Card savedCard = cardRepository.save(card);
        return cardMapper.toDto(savedCard);
    }

    @Override
    public CardDTO getCardById(String cardId, boolean showSensitiveDetails) {
        CardDTO cardDTO = cardRepository.findByCardId(cardId)
                .map(cardMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        
        cardDTO.setShowSensitiveDetails(showSensitiveDetails);
        return cardDTO;
    }

    @Override
    public Page<CardDTO> getAllCards(Pageable pageable) {
        return cardRepository.findAll(pageable)
                .map(card -> {
                    CardDTO dto = cardMapper.toDto(card);
                    dto.setShowSensitiveDetails(false);
                    return dto;
                });
    }

    @Override
    public Page<CardDTO> searchCards(String cardAlias, String cardType, String accountId, Pageable pageable) {
        return cardRepository.searchCards(cardAlias, cardType, accountId, pageable)
                .map(card -> {
                    CardDTO dto = cardMapper.toDto(card);
                    dto.setShowSensitiveDetails(false);
                    return dto;
                });
    }

    @Override
    public CardDTO updateCardAlias(String cardId, CardDTO cardDTO) {
        Card existingCard = cardRepository.findByCardId(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        existingCard.setCardAlias(cardDTO.getCardAlias());
        Card updatedCard = cardRepository.save(existingCard);
        
        CardDTO responseDto = cardMapper.toDto(updatedCard);
        responseDto.setShowSensitiveDetails(false);
        return responseDto;
    }

    @Override
    public void deleteCard(String cardId) {
        Card card = cardRepository.findByCardId(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        cardRepository.delete(card);
    }

    @Override
    public Page<CardDTO> getCardsByAccountId(String accountId, Pageable pageable) {
        return cardRepository.findByAccountId(accountId, pageable)
                .map(card -> {
                    CardDTO dto = cardMapper.toDto(card);
                    dto.setShowSensitiveDetails(false);
                    return dto;
                });
    }

    @Override
    public boolean accountHasCardType(String accountId, String cardType) {
        return cardRepository.countByAccountIdAndCardType(accountId, cardType) > 0;
    }

    private String generateSecurePan() {
        // Implement secure PAN generation logic
        return "4" + UUID.randomUUID().toString().replace("-", "").substring(0, 15);
    }

    private String generateSecureCvv() {
        // Implement secure CVV generation logic
        return String.format("%03d", (int)(Math.random() * 1000));
    }
}
