package com.amos.nyagar.card_service.controller;

import com.amos.nyagar.card_service.service.abstracts.CardService;
import com.amos.nyagar.card_service.entity.DTO.CardDTO;
import com.amos.nyagar.card_service.entity.request.CardSaveRequest;
import com.amos.nyagar.card_service.entity.request.CardUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cards")
@Tag(name = "Card Management", description = "Endpoints for managing payment cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @Operation(
        summary = "Create a new card",
        description = "Creates a new payment card. PAN and CVV are generated server-side."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Card created successfully",
                    content = @Content(schema = @Schema(implementation = CardDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Card limit reached for account")
    })
    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CardDTO> createCard(
            @Parameter(description = "Card creation data", required = true)
            @Valid @RequestBody CardSaveRequest request) {
        CardDTO cardDTO = convertToCardDTO(request);
        CardDTO createdCard = cardService.createCard(cardDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
    }

    @Operation(
        summary = "Get card by ID",
        description = "Retrieves card details. Sensitive data is masked by default."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Card found",
                    content = @Content(schema = @Schema(implementation = CardDTO.class))),
        @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @GetMapping(value = "/{cardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    // @PreAuthorize("hasRole('ADMIN') or (#showSensitiveDetails == false)")
    public ResponseEntity<CardDTO> getCardById(
            @Parameter(description = "ID of the card to retrieve", required = true)
            @PathVariable String cardId,
            
            @Parameter(description = "Show full PAN and CVV (requires special permissions)")
            @RequestParam(defaultValue = "false") boolean showSensitiveDetails) {
        CardDTO card = cardService.getCardById(cardId, showSensitiveDetails);
        return ResponseEntity.ok(card);
    }

    @Operation(
        summary = "Get all cards",
        description = "Retrieves all cards with pagination. Sensitive data is always masked."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved cards",
        content = @Content(schema = @Schema(implementation = Page.class))
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CardDTO>> getAllCards(
            @ParameterObject
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        Page<CardDTO> cards = cardService.getAllCards(pageable);
        return ResponseEntity.ok(cards);
    }

    @Operation(
        summary = "Search cards",
        description = "Search cards with filters. Sensitive data is always masked."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved filtered cards",
        content = @Content(schema = @Schema(implementation = Page.class))
    )
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CardDTO>> searchCards(
            @Parameter(description = "Partial or complete card alias") 
            @RequestParam(required = false) String cardAlias,
            
            @Parameter(description = "Card type (VIRTUAL or PHYSICAL)") 
            @RequestParam(required = false) String cardType,
            
            @Parameter(description = "Account ID to filter by") 
            @RequestParam(required = false) String accountId,
            
            @ParameterObject
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<CardDTO> cards = cardService.searchCards(cardAlias, cardType, accountId, pageable);
        return ResponseEntity.ok(cards);
    }

    @Operation(
        summary = "Update card alias",
        description = "Updates only the card's alias. Other fields cannot be modified."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Card updated successfully",
                    content = @Content(schema = @Schema(implementation = CardDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @PatchMapping(
        value = "/{cardId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CardDTO> updateCardAlias(
            @Parameter(description = "ID of the card to update", required = true)
            @PathVariable String cardId,
            
            @Parameter(description = "Updated card alias", required = true)
            @Valid @RequestBody CardUpdateRequest request) {
        
        CardDTO cardDTO = new CardDTO();
        cardDTO.setCardAlias(request.getCardAlias());
        
        CardDTO updatedCard = cardService.updateCardAlias(cardId, cardDTO);
        return ResponseEntity.ok(updatedCard);
    }

    @Operation(
        summary = "Delete a card",
        description = "Permanently deletes the specified card"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Card deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(
            @Parameter(description = "ID of the card to delete", required = true)
            @PathVariable String cardId) {
        cardService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Get cards by account",
        description = "Retrieves all cards for a specific account"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved account cards",
        content = @Content(schema = @Schema(implementation = Page.class))
    )
    @GetMapping(value = "/account/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CardDTO>> getCardsByAccountId(
            @Parameter(description = "ID of the account", required = true)
            @PathVariable String accountId,
            
            @ParameterObject
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<CardDTO> cards = cardService.getCardsByAccountId(accountId, pageable);
        return ResponseEntity.ok(cards);
    }

    private CardDTO convertToCardDTO(CardSaveRequest request) {
        CardDTO dto = new CardDTO();
        dto.setCardAlias(request.getCardAlias());
        dto.setAccountId(request.getAccountId());
        dto.setCardType(request.getCardType());
        return dto;
    }
}