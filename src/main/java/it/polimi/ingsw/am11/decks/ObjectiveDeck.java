package it.polimi.ingsw.am11.decks;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;

import java.util.ArrayList;

public class ObjectiveDeck implements Deck<ObjectiveCard> {

    private ArrayList<ObjectiveCard> objectiveDeck;

    @Override
    public Deck<ObjectiveCard> shuffle() {
        return null;
    }

    @Override
    public ObjectiveCard drawCard() {
        return null;
    }

    @Override
    public int getRemainingCards() {
        return 0;
    }

    @Override
    public void addCard(ObjectiveCard card) {

    }

    @Override
    public DeckType getDeckType() {
        return UtilitiesDeckType.OBJECTIVE;
    }
}
