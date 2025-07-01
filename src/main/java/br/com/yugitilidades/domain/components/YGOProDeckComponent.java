package br.com.yugitilidades.domain.components;

import br.com.yugitilidades.domain.mappers.CardMapper;
import br.com.yugitilidades.domain.model.Card;
import br.com.yugitilidades.domain.model.DTOs.YgoProDeckResponse;
import io.github.bucket4j.Bucket;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.springframework.beans.factory.annotation.Qualifier;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Log4j2
@Component
public class YGOProDeckComponent {
    private final String cardInfoUrl;
    private final String dbVersionUrl;
    private final RestTemplate restTemplate;
    private final CardMapper cardMapper;
    private final Bucket rateLimiter;

    public YGOProDeckComponent(
            @Value("${ygo-prodeck.api.cardinfo}") String cardInfoUrl,
            @Value("${ygo-prodeck.api.db.version}") String dbVersionUrl,
            RestTemplate restTemplate,
            CardMapper cardMapper,
            @Qualifier("ygoProDeckBucket") Bucket rateLimiter) {
        this.cardInfoUrl = cardInfoUrl;
        this.dbVersionUrl = dbVersionUrl;
        this.restTemplate = restTemplate;
        this.cardMapper = cardMapper;
        this.rateLimiter = rateLimiter;
    }

    public Optional<Card> getCardByName(String name) {
        if (!rateLimiter.tryConsume(1)) {
            log.warn("YGOProDeck API rate limit exceeded. Try again later.");
            return Optional.empty();
        }
        try {
            String url = cardInfoUrl + "?name=" + name;
            YgoProDeckResponse response = restTemplate.getForObject(url, YgoProDeckResponse.class);
            if (response != null && response.getData() != null && !response.getData().isEmpty()) {
                return Optional.of(cardMapper.ygoProDeckCardDtoToCard(response.getData().get(0)));
            }
        } catch (HttpClientErrorException.BadRequest e) {
            log.warn("Card not found in YGOProDeck API: {}", name);
        } catch (Exception e) {
            log.error("Error while fetching card from YGOProDeck API", e);
        }
        return Optional.empty();
    }

    public Optional<List<Card>> getAllCards() {
        if (!rateLimiter.tryConsume(1)) {
            log.warn("YGOProDeck API rate limit exceeded. Try again later.");
            return Optional.empty();
        }
        try {
            YgoProDeckResponse response = restTemplate.getForObject(cardInfoUrl, YgoProDeckResponse.class);
            if (response != null && response.getData() != null && !response.getData().isEmpty()) {
                return Optional.of(cardMapper.ygoProDeckCardDtoToCard(response.getData()));
            }
        } catch (HttpClientErrorException.BadRequest e) {
            log.warn("No cards found in YGOProDeck API");
        } catch (Exception e) {
            log.error("Error while fetching all cards from YGOProDeck API", e);
        }
        return Optional.empty();
    }

    public Optional<String> getCurrentApiVersion() {
        if (!rateLimiter.tryConsume(1)) {
            log.warn("YGOProDeck API rate limit exceeded. Try again later.");
            return Optional.empty();
        }
        try {
            var response = restTemplate.getForObject(dbVersionUrl, java.util.List.class);
            if (response != null && !response.isEmpty()) {
                var versionInfo = (java.util.Map<?, ?>) response.get(0);
                return Optional.ofNullable(versionInfo.get("database_version").toString());
            }
        } catch (Exception e) {
            log.error("Error fetching YGOProDeck API version", e);
        }
        return Optional.empty();
    }
}
