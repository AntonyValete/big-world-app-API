package br.com.yugitilidades.domain.components;

import br.com.yugitilidades.domain.mappers.CardMapper;
import br.com.yugitilidades.domain.model.Card;
import br.com.yugitilidades.domain.model.DTOs.YgoProDeckResponse;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class YGOProDeckComponentTest {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private CardMapper cardMapper;
    @Mock
    private Bucket rateLimiter;

    private YGOProDeckComponent component;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        component = new YGOProDeckComponent(
                "http://fake-url/cardinfo",
                "http://fake-url/dbversion",
                restTemplate,
                cardMapper,
                rateLimiter
        );
    }

    @Test
    void getCardByName_rateLimitExceeded_returnsEmpty() {
        when(rateLimiter.tryConsume(1)).thenReturn(false);
        Optional<Card> result = component.getCardByName("Blue-Eyes");
        assertTrue(result.isEmpty());
    }

    @Test
    void getCardByName_cardFound_returnsCard() {
        when(rateLimiter.tryConsume(1)).thenReturn(true);
        YgoProDeckResponse response = new YgoProDeckResponse();
        YgoProDeckResponse.YgoProDeckCardDTO dto = mock(YgoProDeckResponse.YgoProDeckCardDTO.class);
        response.setData(List.of(dto));
        Card card = mock(Card.class);
        when(restTemplate.getForObject(anyString(), eq(YgoProDeckResponse.class))).thenReturn(response);
        when(cardMapper.ygoProDeckCardDtoToCard(any(br.com.yugitilidades.domain.model.DTOs.YgoProDeckResponse.YgoProDeckCardDTO.class))).thenReturn(card);
        Optional<Card> result = component.getCardByName("Blue-Eyes");
        assertTrue(result.isPresent());
        assertEquals(card, result.get());
    }

    @Test
    void getCardByName_cardNotFound_returnsEmpty() {
        when(rateLimiter.tryConsume(1)).thenReturn(true);
        when(restTemplate.getForObject(anyString(), eq(YgoProDeckResponse.class))).thenReturn(null);
        Optional<Card> result = component.getCardByName("NonExistent");
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllCards_rateLimitExceeded_returnsEmpty() {
        when(rateLimiter.tryConsume(1)).thenReturn(false);
        Optional<List<Card>> result = component.getAllCards();
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllCards_cardsFound_returnsList() {
        when(rateLimiter.tryConsume(1)).thenReturn(true);
        YgoProDeckResponse response = new YgoProDeckResponse();
        YgoProDeckResponse.YgoProDeckCardDTO dto = mock(YgoProDeckResponse.YgoProDeckCardDTO.class);
        response.setData(List.of(dto));
        List<Card> cards = List.of(mock(Card.class));
        when(restTemplate.getForObject(anyString(), eq(YgoProDeckResponse.class))).thenReturn(response);
        when(cardMapper.ygoProDeckCardDtoToCard(anyList())).thenReturn(cards);
        Optional<List<Card>> result = component.getAllCards();
        assertTrue(result.isPresent());
        assertEquals(cards, result.get());
    }

    @Test
    void getCurrentApiVersion_rateLimitExceeded_returnsEmpty() {
        when(rateLimiter.tryConsume(1)).thenReturn(false);
        Optional<String> result = component.getCurrentApiVersion();
        assertTrue(result.isEmpty());
    }

    @Test
    void getCurrentApiVersion_versionFound_returnsVersion() {
        when(rateLimiter.tryConsume(1)).thenReturn(true);
        List<Object> response = List.of(new java.util.HashMap<String, Object>() {{
            put("database_version", "1.2.3");
        }});
        when(restTemplate.getForObject(anyString(), eq(List.class))).thenReturn(response);
        Optional<String> result = component.getCurrentApiVersion();
        assertTrue(result.isPresent());
        assertEquals("1.2.3", result.get());
    }
}
