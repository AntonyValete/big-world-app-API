package br.com.yugitilidades.controllers;

import br.com.yugitilidades.services.CardService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final CardService cardService;

    public AdminController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/importCards")
    public ResponseEntity<String> importAllCards() {
        cardService.importAllCardsFromYGOProDeck();
        return ResponseEntity.accepted().body("Card import started (check logs for progress).");
    }

    @GetMapping("/importStatus")
    public ResponseEntity<String> getImportStatus() {
        long count = cardService.countCards();
        if (count > 0) {
            return ResponseEntity.ok("Import succeeded. Total cards in DB: " + count);
        } else {
            return ResponseEntity.ok("Import not completed or failed. No cards in DB.");
        }
    }
}
