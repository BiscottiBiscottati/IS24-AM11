package it.polimi.ingsw.am11.decks;

import it.polimi.ingsw.am11.cards.playable.ResourceCard;

import java.util.ArrayList;

public class ResourceDeck implements Deck<ResourceCard> {

    private ArrayList<ResourceCard> resourceDeck;

    @Override
    public Deck<ResourceCard> shuffle() {
        return null;
    }

    @Override
    public ResourceCard drawCard() {
        return null;
    }

    @Override
    public int getRemainingCards() {
        return 0;
    }

    @Override
    public void addCard(ResourceCard card) {

    }

    @Override
    public DeckType getDeckType() {
        return PlayableDeckType.RESOURCE;
    }
}
