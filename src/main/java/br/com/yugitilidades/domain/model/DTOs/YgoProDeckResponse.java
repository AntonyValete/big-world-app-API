package br.com.yugitilidades.domain.model.DTOs;

import br.com.yugitilidades.domain.model.CardImages;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class YgoProDeckResponse {
    private List<YgoProDeckCardDTO> data;

    @Data
    public static class YgoProDeckCardDTO {
        private Long id;
        private String name;
        private String type;
        private String frameType;
        @JsonProperty("desc")
        private String description;
        private Integer atk;
        private Integer def;
        private Integer level;
        private String race;
        private String attribute;
        private String archetype;
        @JsonProperty("card_images")
        private List<CardImages> cardImages;
    }
}