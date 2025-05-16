package com.amos.nyagar.card_service.service.abstracts.mapper;

import com.amos.nyagar.card_service.entity.Card;
import com.amos.nyagar.card_service.entity.DTO.CardDTO;
import com.amos.nyagar.card_service.entity.request.CardSaveRequest;
import com.amos.nyagar.card_service.entity.request.CardUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CardMapper {
    CardDTO toDto(Card card);
    Card toEntity(CardDTO cardDTO);
    
    @Mapping(target = "cardId", ignore = true)
    @Mapping(target = "pan", ignore = true)
    @Mapping(target = "cvv", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Card toEntityFromSaveRequest(CardSaveRequest request);
    
    @Mapping(target = "cardId", ignore = true)
    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "cardType", ignore = true)
    @Mapping(target = "pan", ignore = true)
    @Mapping(target = "cvv", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Card toEntityFromUpdateRequest(CardUpdateRequest request);
}
