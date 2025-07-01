package br.com.yugitilidades.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardImages {
    @JsonProperty("image_url")
    private String imageUrl;
    
    @JsonProperty("image_url_small")
    private String imageUrlSmall;
}
