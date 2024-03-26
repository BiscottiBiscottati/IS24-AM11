package it.polimi.ingsw.am11.decks;

public interface DeckFactory<T> {
    Deck<T> createDeck();
}
