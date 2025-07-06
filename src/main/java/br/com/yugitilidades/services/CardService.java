package br.com.yugitilidades.services;

import br.com.yugitilidades.domain.components.YGOProDeckComponent;
import br.com.yugitilidades.domain.model.Card;
import br.com.yugitilidades.domain.model.DTOs.CardSummaryResponse;
import br.com.yugitilidades.domain.repositories.CardRepository;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Log4j2
public class CardService {
    private final CardRepository cardRepository;
    private final YGOProDeckComponent ygoProDeckComponent;
    private final R2Service r2Service;
    private final RestTemplate restTemplate;

    public CardService(CardRepository cardRepository,
            YGOProDeckComponent ygoProDeckComponent,
            R2Service r2Service,
            RestTemplate restTemplate) {
        this.cardRepository = cardRepository;
        this.ygoProDeckComponent = ygoProDeckComponent;
        this.r2Service = r2Service;
        this.restTemplate = restTemplate;

    }

    public Card ensureCardImagesInR2(Card card) {
        if (card.getR2ImageUrl() == null || card.getR2ImageSmallUrl() == null ||
                card.getR2ImageUrl().startsWith("http") || card.getR2ImageSmallUrl().startsWith("http")) {
            try {
                byte[] fullImage = restTemplate.getForObject(card.getR2ImageUrl(), byte[].class);
                byte[] smallImage = restTemplate.getForObject(card.getR2ImageSmallUrl(), byte[].class);
                String fullImageName = card.getId() + ".jpg";
                String smallImageName = card.getId() + "_small.jpg";
                r2Service.uploadFile(fullImage, fullImageName);
                r2Service.uploadFile(smallImage, smallImageName);
                card.setR2ImageUrl(fullImageName);
                card.setR2ImageSmallUrl(smallImageName);
                cardRepository.save(card);
            } catch (Exception e) {
                log.error("Error downloading/uploading images for card {}: {}", card.getName(), e.getMessage());
            }
        }
        return card;
    }

    public Card findCardByName(String name) {
        log.info("Searching for card by name: {}", name);
        Card card = cardRepository.findByName(name)
                .orElseGet(() -> {
                    Card cardFromApi = ygoProDeckComponent.getCardByName(name)
                            .orElseThrow(() -> new RuntimeException("Card not found"));
                    return cardRepository.save(cardFromApi);
                });
        return ensureCardImagesInR2(card);
    }

    public Card findCardById(Long id) {
        log.info("Searching for card by id: {}", id);
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        return ensureCardImagesInR2(card);
    }

    @Async
    public void importAllCardsFromYGOProDeck() {
        log.info("Starting bulk import of all cards from YGOProDeck");
        var apiVersionOpt = ygoProDeckComponent.getCurrentApiVersion();
        if (apiVersionOpt.isEmpty()) {
            log.error("Could not fetch YGOProDeck API version. Aborting import.");
            return;
        }
        String apiVersion = apiVersionOpt.get();
        ygoProDeckComponent.getAllCards().ifPresent(cards -> {
            cards.forEach(card -> card.setApiVersion(apiVersion));
            cardRepository.saveAll(cards);
            log.info("Imported {} cards from YGOProDeck in bulk.", cards.size());
        });
    }

    public long countCards() {
        return cardRepository.count();
    }

    public List<CardSummaryResponse> getAllCardSummaries(String name) {
        List<Card> cards;
        if (name != null && !name.isBlank()) {
            cards = cardRepository.findByNameContainingIgnoreCase(name);
        } else {
            cards = cardRepository.findAll();
        }
        return cards.stream()
                .map(card -> new CardSummaryResponse(
                        card.getId(),
                        card.getName()))
                .toList();
    }
}
