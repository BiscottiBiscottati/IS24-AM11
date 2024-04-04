package it.polimi.ingsw.am11.decks.starter;

import com.google.common.collect.ImmutableMap;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.utils.DeckType;

import java.util.ArrayList;
import java.util.Optional;

public class StarterDeck implements Deck<StarterCard> {

    private final ImmutableMap<Integer, StarterCard> mappingIdToCard;

    private ArrayList<StarterCard> starterDeck;

    public StarterDeck(ImmutableMap<Integer, StarterCard> mappingIdToCard) {
        this.mappingIdToCard = mappingIdToCard;
    }

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
        return DeckType.STARTER;
    }

    @Override
    public void reset() {

    }

    @Override
    public Optional<StarterCard> getCardById(int id) {
        return Optional.ofNullable(mappingIdToCard.getOrDefault(id, null));
    }
}
