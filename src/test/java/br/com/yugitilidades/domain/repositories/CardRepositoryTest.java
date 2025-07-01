package br.com.yugitilidades.domain.repositories;

import br.com.yugitilidades.domain.model.Card;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CardRepositoryTest {
    @Autowired
    private CardRepository cardRepository;

    @Test
    void saveAndFindByName() {
        Card card = new Card();
        card.setId(1001L); // Manual ID assignment
        card.setName("TestCard");
        cardRepository.save(card);
        Optional<Card> found = cardRepository.findByName("TestCard");
        assertTrue(found.isPresent());
        assertEquals("TestCard", found.get().getName());
        assertEquals(1001L, found.get().getId());
    }

    @Test
    void findByNameContainingIgnoreCase() {
        Card card1 = new Card();
        card1.setId(2001L); // Manual ID assignment
        card1.setName("Blue-Eyes White Dragon");
        Card card2 = new Card();
        card2.setId(2002L); // Manual ID assignment
        card2.setName("blue-eyes ultimate dragon");
        cardRepository.save(card1);
        cardRepository.save(card2);
        var results = cardRepository.findByNameContainingIgnoreCase("blue-eyes");
        assertEquals(2, results.size());
    }
}
// Refactored to use manual ID assignment for Card entity, compatible with import logic.
