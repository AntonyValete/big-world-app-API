package br.com.yugitilidades.domain.model;

import br.com.yugitilidades.domain.model.enums.CardAttributeEnum;
import br.com.yugitilidades.domain.model.enums.CardRaceEnum;
import br.com.yugitilidades.domain.model.enums.CardTypeEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card implements Serializable {

    @Id
    private Long id;

    private String name;

    private CardTypeEnum type;

    private String frameType;

    @jakarta.persistence.Column(length = 2048)
    private String description;

    private Integer atk;

    private Integer def;

    private Integer level;

    private CardRaceEnum race;

    private CardAttributeEnum attribute;

    @jakarta.persistence.Column(length = 512)
    private String archetype;

    @jakarta.persistence.Column(length = 2048)
    private String r2ImageUrl;
    @jakarta.persistence.Column(length = 2048)
    private String r2ImageSmallUrl;

    private String apiVersion;
}
