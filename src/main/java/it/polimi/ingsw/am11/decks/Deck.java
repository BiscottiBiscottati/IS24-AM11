package it.polimi.ingsw.am11.decks;

import it.polimi.ingsw.am11.decks.utils.DeckType;

import java.util.Optional;

public interface Deck<T> {
    Deck<T> shuffle();

    T drawCard();

    int getRemainingCards();

    void addCard(T card);

    DeckType getDeckType();

    void reset();

    Optional<T> getCardById(int id);
}
