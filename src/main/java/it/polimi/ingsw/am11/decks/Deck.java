package it.polimi.ingsw.am11.decks;

public interface Deck<T> {
    Deck<T> shuffle();

    T drawCard();

    int getRemainingCards();

    void addCard(T card);

    DeckType getDeckType();
}
