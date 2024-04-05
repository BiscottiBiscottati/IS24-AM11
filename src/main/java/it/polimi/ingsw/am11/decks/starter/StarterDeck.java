package it.polimi.ingsw.am11.decks.starter;

import com.google.common.collect.ImmutableMap;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.utils.DeckType;
import it.polimi.ingsw.am11.decks.utils.UtilitiesDeckType;

import java.util.Collections;
import java.util.Optional;
import java.util.Stack;

public class StarterDeck implements Deck<StarterCard> {

    private final ImmutableMap<Integer, StarterCard> mappingIdToCard;

    private final Stack<StarterCard> starterDeck;

    public StarterDeck(ImmutableMap<Integer, StarterCard> mappingIdToCard) {
        this.mappingIdToCard = mappingIdToCard;
        this.starterDeck = new Stack<>();
    }

    @Override
    public Deck<StarterCard> shuffle() {
        Collections.shuffle(this.starterDeck);
        return this;
    }

    @Override
    public StarterCard drawCard() {
        return this.starterDeck.pop();
    }

    @Override
    public int getRemainingCards() {
        return this.starterDeck.size();
    }

    @Override
    public void addCard(StarterCard card) {
        this.starterDeck.push(card);
    }

    @Override
    public DeckType getDeckType() {
        return UtilitiesDeckType.STARTER;
    }

    @Override
    public void reset() {
        this.starterDeck.clear();
        this.starterDeck.addAll(mappingIdToCard.values());
    }

    @Override
    public Optional<StarterCard> getCardById(int id) {
        return Optional.ofNullable(mappingIdToCard.getOrDefault(id, null));
    }
}
