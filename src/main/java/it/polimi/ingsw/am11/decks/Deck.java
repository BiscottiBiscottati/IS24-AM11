package it.polimi.ingsw.am11.decks;

import it.polimi.ingsw.am11.decks.utils.DeckType;

public interface Deck<T> {
    Deck<T> shuffle();

    T drawCard();

    int getRemainingCards();

    void addCard(T card);

    DeckType getDeckType();

    void reset();

    T getCardbyId(int id);
}
