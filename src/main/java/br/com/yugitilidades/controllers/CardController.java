package br.com.yugitilidades.controllers;

import br.com.yugitilidades.domain.mappers.CardMapper;
import br.com.yugitilidades.domain.model.DTOs.CardResponse;
import br.com.yugitilidades.domain.model.DTOs.CardSummaryResponse;
import br.com.yugitilidades.services.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {
    private final CardService cardService;
    private final CardMapper cardMapper;

    public CardController(CardService cardService, CardMapper cardMapper) {
        this.cardService = cardService;
        this.cardMapper = cardMapper;
    }

    @GetMapping("/{name}")
    public ResponseEntity<CardResponse> getCardByName(@PathVariable String name) {
        var card = cardService.findCardByName(name);
        return ResponseEntity.ok(cardMapper.cardToCardResponse(card));
    }

    @GetMapping("/summary")
    public ResponseEntity<List<CardSummaryResponse>> getAllCardSummaries(@RequestParam(required = false) String name) {
        List<CardSummaryResponse> summaries = cardService.getAllCardSummaries(name);
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CardResponse> getCardById(@PathVariable Long id) {
        var card = cardService.findCardById(id);
        return ResponseEntity.ok(cardMapper.cardToCardResponse(card));
    }
}
