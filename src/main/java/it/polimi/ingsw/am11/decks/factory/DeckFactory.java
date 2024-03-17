package it.polimi.ingsw.am11.decks.factory;

import it.polimi.ingsw.am11.decks.Deck;

interface DeckFactory<T> {
    Deck<T> createDeck();
}
