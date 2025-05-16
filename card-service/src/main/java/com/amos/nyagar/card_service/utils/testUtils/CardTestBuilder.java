package com.amos.nyagar.card_service.utils.testUtils;

import com.amos.nyagar.card_service.entity.Card;
import com.amos.nyagar.card_service.entity.DTO.CardDTO;

import java.util.UUID;

public class CardTestBuilder {

    public static Card buildCard() {
        Card card = new Card();
        card.setCardId("card-" + UUID.randomUUID().toString());
        card.setCardAlias("Test Card");
        card.setAccountId("acc-123");
        card.setCardType("PHYSICAL");
        card.setPan("1234567890123456");
        card.setCvv("123");
        return card;
    }

    public static CardDTO buildCardDTO() {
        Card card = buildCard();
        CardDTO dto = new CardDTO();
        dto.setCardId(card.getCardId());
        dto.setCardAlias(card.getCardAlias());
        dto.setAccountId(card.getAccountId());
        dto.setCardType(card.getCardType());
        dto.setPan(card.getPan());
        dto.setCvv(card.getCvv());
        return dto;
    }
}
