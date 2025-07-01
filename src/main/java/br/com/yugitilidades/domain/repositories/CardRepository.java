package br.com.yugitilidades.domain.repositories;

import br.com.yugitilidades.domain.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByName(String name);
    List<Card> findByNameContainingIgnoreCase(String name);
}