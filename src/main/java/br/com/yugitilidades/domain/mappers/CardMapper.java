package br.com.yugitilidades.domain.mappers;

import br.com.yugitilidades.domain.model.Card;
import br.com.yugitilidades.domain.model.DTOs.YgoProDeckResponse.YgoProDeckCardDTO;
import br.com.yugitilidades.domain.model.DTOs.CardResponse;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {
    @Mapping(target = "type", expression = "java(mapType(dto.getType()))")
    @Mapping(target = "race", expression = "java(mapRace(dto.getRace()))")
    @Mapping(target = "attribute", expression = "java(mapAttribute(dto.getAttribute()))")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "r2ImageUrl", expression = "java(mapImageUrl(dto))")
    @Mapping(target = "r2ImageSmallUrl", expression = "java(mapImageSmallUrl(dto))")
    @Mapping(target = "apiVersion", ignore = true)
    Card ygoProDeckCardDtoToCard(YgoProDeckCardDTO dto);

    List<Card> ygoProDeckCardDtoToCard(List<YgoProDeckCardDTO> dtos);

    default String mapImageUrl(YgoProDeckCardDTO dto) {
        if (dto.getCardImages() != null && !dto.getCardImages().isEmpty()) {
            return dto.getCardImages().get(0).getImageUrl();
        }
        return null;
    }

    default String mapImageSmallUrl(YgoProDeckCardDTO dto) {
        if (dto.getCardImages() != null && !dto.getCardImages().isEmpty()) {
            return dto.getCardImages().get(0).getImageUrlSmall();
        }
        return null;
    }

    default br.com.yugitilidades.domain.model.enums.CardAttributeEnum mapAttribute(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return br.com.yugitilidades.domain.model.enums.CardAttributeEnum.fromValue(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    default br.com.yugitilidades.domain.model.enums.CardRaceEnum mapRace(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return br.com.yugitilidades.domain.model.enums.CardRaceEnum.fromValue(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    default br.com.yugitilidades.domain.model.enums.CardTypeEnum mapType(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return br.com.yugitilidades.domain.model.enums.CardTypeEnum.fromValue(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "type", expression = "java(card.getType() != null ? card.getType().getValue() : null)")
    @Mapping(target = "race", expression = "java(card.getRace() != null ? card.getRace().getValue() : null)")
    @Mapping(target = "attribute", expression = "java(card.getAttribute() != null ? card.getAttribute().getValue() : null)")
    @Mapping(target = "atk", source = "atk")
    @Mapping(target = "def", source = "def")
    @Mapping(target = "level", source = "level")
    @Mapping(target = "desc", source = "description")
    @Mapping(target = "imageUrl", expression = "java(card.getR2ImageUrl() != null ? card.getR2ImageUrl() : \"https://images.ygoprodeck.com/images/cards/\" + card.getId() + \".jpg\")")
    CardResponse cardToCardResponse(Card card);
}