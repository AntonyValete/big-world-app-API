package br.com.yugitilidades.services;

import br.com.yugitilidades.domain.components.YGOProDeckComponent;
import br.com.yugitilidades.domain.model.Card;
import br.com.yugitilidades.domain.repositories.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CardServiceTest {
    private CardRepository cardRepository;
    private YGOProDeckComponent ygoProDeckComponent;
    private R2Service r2Service;
    private RestTemplate restTemplate;
    private CardService cardService;

    @BeforeEach
    void setUp() {
        cardRepository = Mockito.mock(CardRepository.class);
        ygoProDeckComponent = Mockito.mock(YGOProDeckComponent.class);
        r2Service = Mockito.mock(R2Service.class);
        restTemplate = Mockito.mock(RestTemplate.class);
        cardService = new CardService(
                cardRepository,
                ygoProDeckComponent,
                r2Service,
                restTemplate
        );
    }

    @Test
    void ensureCardImagesInR2_uploadsIfNeeded() {
        Card card = new Card();
        card.setId(1L);
        card.setR2ImageUrl("http://someurl");
        card.setR2ImageSmallUrl("http://someurl");
        when(restTemplate.getForObject(anyString(), eq(byte[].class))).thenReturn(new byte[]{1,2,3});
        when(r2Service.uploadFile(any(), anyString())).thenReturn("ok");
        when(cardRepository.save(any())).thenReturn(card);
        Card result = cardService.ensureCardImagesInR2(card);
        assertEquals(card, result);
        verify(r2Service, times(2)).uploadFile(any(), anyString());
        verify(cardRepository).save(card);
    }

    @Test
    void ensureCardImagesInR2_doesNotUploadIfAlreadySet() {
        Card card = new Card();
        card.setId(1L);
        card.setR2ImageUrl("1.jpg");
        card.setR2ImageSmallUrl("1_small.jpg");
        Card result = cardService.ensureCardImagesInR2(card);
        assertEquals(card, result);
        verifyNoInteractions(restTemplate, r2Service, cardRepository);
    }
}
