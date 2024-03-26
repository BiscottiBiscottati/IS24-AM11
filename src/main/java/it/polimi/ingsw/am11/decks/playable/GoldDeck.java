package it.polimi.ingsw.am11.decks.playable;

import it.polimi.ingsw.am11.cards.playable.GoldCard;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.utils.DeckType;
import it.polimi.ingsw.am11.decks.utils.PlayableDeckType;

import java.util.ArrayList;

public class GoldDeck implements Deck<GoldCard> {

    private ArrayList<GoldCard> goldDeck;

    @Override
    public Deck<GoldCard> shuffle() {
        return null;
    }

    @Override
    public GoldCard drawCard() {
        return null;
    }

    @Override
    public int getRemainingCards() {
        return 0;
    }

    @Override
    public void addCard(GoldCard card) {

    }

    @Override
    public DeckType getDeckType() {
        return PlayableDeckType.GOLD;
    }
}
