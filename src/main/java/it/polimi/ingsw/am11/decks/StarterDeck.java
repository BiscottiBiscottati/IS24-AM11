package it.polimi.ingsw.am11.decks;

import it.polimi.ingsw.am11.cards.starter.StarterCard;

import java.util.ArrayList;

public class StarterDeck implements Deck<StarterCard> {

    private ArrayList<StarterCard> starterDeck;

    @Override
    public Deck<StarterCard> shuffle() {
        return null;
    }

    @Override
    public StarterCard drawCard() {
        return null;
    }

    @Override
    public int getRemainingCards() {
        return 0;
    }

    @Override
    public void addCard(StarterCard card) {

    }

    @Override
    public DeckType getDeckType() {
        return UtilitiesDeckType.STARTER;
    }
}
