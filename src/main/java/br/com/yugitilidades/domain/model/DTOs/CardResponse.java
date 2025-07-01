package br.com.yugitilidades.domain.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardResponse {
    private Long id;
    private String name;
    private String type;
    private String race;
    private Integer atk;
    private Integer def;
    private Integer level;
    private String desc;
    private String imageUrl;
}
