package it.polimi.ingsw.am11.decks.playable;

import it.polimi.ingsw.am11.cards.playable.GoldCard;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.utils.DeckType;
import it.polimi.ingsw.am11.decks.utils.PlayableDeckType;

import java.util.Collections;
import java.util.Stack;

public class GoldDeck implements Deck<GoldCard> {

    private final Stack<GoldCard> goldDeck;

    public GoldDeck() {
        goldDeck = new Stack<>();
    }

    @Override
    public Deck<GoldCard> shuffle() {
        Collections.shuffle(goldDeck);
        return this;
    }

    @Override
    public GoldCard drawCard() {
        return goldDeck.pop();
    }

    public boolean isEmpty() {
        return goldDeck.isEmpty();
    }

    @Override
    public int getRemainingCards() {
        return goldDeck.size();
    }

    @Override
    public void addCard(GoldCard card) {
        goldDeck.push(card);
    }

    @Override
    public DeckType getDeckType() {
        return PlayableDeckType.GOLD;
    }
}
