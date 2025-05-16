package com.amos.nyagar.card_service;

import com.amos.nyagar.card_service.entity.Card;
import com.amos.nyagar.card_service.entity.DTO.CardDTO;
import com.amos.nyagar.card_service.repository.CardRepository;
import com.amos.nyagar.card_service.service.abstracts.mapper.CardMapperImpl;
import com.amos.nyagar.card_service.service.concrete.CardServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardMapperImpl cardMapper;

    @InjectMocks
    private CardServiceImpl cardService;

    private Card card;
    private CardDTO cardDTO;

    @BeforeEach
    void setUp() {
        card = new Card();
        card.setCardId("card-" + UUID.randomUUID().toString());
        card.setCardAlias("My Primary Card");
        card.setAccountId("acc-123");
        card.setCardType("PHYSICAL");
        card.setPan("1234567890123456");
        card.setCvv("123");

        cardDTO = new CardDTO();
        cardDTO.setCardId(card.getCardId());
        cardDTO.setCardAlias(card.getCardAlias());
        cardDTO.setAccountId(card.getAccountId());
        cardDTO.setCardType(card.getCardType());
        cardDTO.setPan(card.getPan());
        cardDTO.setCvv(card.getCvv());
    }

    @Test
    void createCard_ShouldReturnCreatedCard_WhenValid() {
        // Arrange
        when(cardMapper.toEntity(any(CardDTO.class))).thenReturn(card);
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        when(cardMapper.toDto(any(Card.class))).thenReturn(cardDTO);
        when(cardRepository.countByAccountIdAndCardType(anyString(), anyString())).thenReturn(0);

        // Act
        CardDTO result = cardService.createCard(cardDTO);

        // Assert
        assertNotNull(result);
        assertEquals(cardDTO.getCardId(), result.getCardId());
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void createCard_ShouldThrowException_WhenCardLimitReached() {
        // Arrange
        when(cardRepository.countByAccountIdAndCardType(anyString(), anyString())).thenReturn(1);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> 
            cardService.createCard(cardDTO));
        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    void getCardById_ShouldReturnCard_WhenExists() {
        // Arrange
        when(cardRepository.findByCardId(anyString())).thenReturn(Optional.of(card));
        when(cardMapper.toDto(any(Card.class))).thenReturn(cardDTO);

        // Act
        CardDTO result = cardService.getCardById(card.getCardId(), false);

        // Assert
        assertNotNull(result);
        assertEquals("1234********3456", result.getPan()); // Verify masking
        assertEquals("***", result.getCvv());
    }

    @Test
    void getCardById_ShouldShowUnmasked_WhenRequested() {
        // Arrange
        when(cardRepository.findByCardId(anyString())).thenReturn(Optional.of(card));
        when(cardMapper.toDto(any(Card.class))).thenReturn(cardDTO);

        // Act
        CardDTO result = cardService.getCardById(card.getCardId(), true);

        // Assert
        assertNotNull(result);
        assertEquals(card.getPan(), result.getPan());
        assertEquals(card.getCvv(), result.getCvv());
    }

    @Test
    void getAllCards_ShouldReturnPaginatedResults() {
        // Arrange
        Page<Card> cardPage = new PageImpl<>(Collections.singletonList(card));
        when(cardRepository.findAll(any(Pageable.class))).thenReturn(cardPage);
        when(cardMapper.toDto(any(Card.class))).thenReturn(cardDTO);

        // Act
        Page<CardDTO> result = cardService.getAllCards(Pageable.unpaged());

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("1234********3456", result.getContent().get(0).getPan());
    }

    @Test
    void searchCards_ShouldReturnFilteredResults() {
        // Arrange
        Page<Card> cardPage = new PageImpl<>(Collections.singletonList(card));
        when(cardRepository.searchCards(any(), any(), any(), any())).thenReturn(cardPage);
        when(cardMapper.toDto(any(Card.class))).thenReturn(cardDTO);

        // Act
        Page<CardDTO> result = cardService.searchCards("Primary", "PHYSICAL", "acc-123", Pageable.unpaged());

        // Assert
        assertEquals(1, result.getTotalElements());
        verify(cardRepository, times(1))
            .searchCards("Primary", "PHYSICAL", "acc-123", Pageable.unpaged());
    }

    @Test
    void updateCardAlias_ShouldUpdateOnlyAlias() {
        // Arrange
        CardDTO updateDTO = new CardDTO();
        updateDTO.setCardAlias("Updated Alias");

        when(cardRepository.findByCardId(anyString())).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        when(cardMapper.toDto(any(Card.class))).thenReturn(cardDTO);

        // Act
        CardDTO result = cardService.updateCardAlias(card.getCardId(), updateDTO);

        // Assert
        assertEquals("Updated Alias", card.getCardAlias());
        assertNotEquals(updateDTO.getPan(), result.getPan()); // PAN shouldn't change
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void deleteCard_ShouldDeleteCard() {
        // Arrange
        when(cardRepository.findByCardId(anyString())).thenReturn(Optional.of(card));
        doNothing().when(cardRepository).delete(any(Card.class));

        // Act
        cardService.deleteCard(card.getCardId());

        // Assert
        verify(cardRepository, times(1)).delete(card);
    }

    @Test
    void getCardsByAccountId_ShouldReturnAccountCards() {
        // Arrange
        Page<Card> cardPage = new PageImpl<>(Collections.singletonList(card));
        when(cardRepository.findByAccountId(anyString(), any())).thenReturn(cardPage);
        when(cardMapper.toDto(any(Card.class))).thenReturn(cardDTO);

        // Act
        Page<CardDTO> result = cardService.getCardsByAccountId("acc-123", Pageable.unpaged());

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("acc-123", result.getContent().get(0).getAccountId());
    }

    @Test
    void accountHasCardType_ShouldReturnTrue_WhenCardExists() {
        // Arrange
        when(cardRepository.countByAccountIdAndCardType(anyString(), anyString())).thenReturn(1);

        // Act
        boolean result = cardService.accountHasCardType("acc-123", "PHYSICAL");

        // Assert
        assertTrue(result);
    }
}
