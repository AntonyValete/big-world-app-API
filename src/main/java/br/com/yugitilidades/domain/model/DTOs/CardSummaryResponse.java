package br.com.yugitilidades.domain.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardSummaryResponse {
    private Long id;
    private String name;
}
