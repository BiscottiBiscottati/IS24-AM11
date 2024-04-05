package it.polimi.ingsw.am11.decks.playable;

import com.google.common.collect.ImmutableMap;
import it.polimi.ingsw.am11.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.utils.DeckType;

import java.util.Collections;
import java.util.Optional;
import java.util.Stack;

public class ResourceDeck implements Deck<ResourceCard> {

    private final ImmutableMap<Integer, ResourceCard> mappingIdToCard;
    private final Stack<ResourceCard> resourceDeck;

    public ResourceDeck(ImmutableMap<Integer, ResourceCard> mappingIdToCard) {
        this.mappingIdToCard = mappingIdToCard;
        this.resourceDeck = new Stack<>();
    }

    @Override
    public Deck<ResourceCard> shuffle() {
        Collections.shuffle(this.resourceDeck);
        return this;
    }

    @Override
    public ResourceCard drawCard() {
        return resourceDeck.pop();
    }

    @Override
    public int getRemainingCards() {
        return resourceDeck.size();
    }

    @Override
    public void addCard(ResourceCard card) {
        resourceDeck.push(card);
    }

    @Override
    public DeckType getDeckType() {
        return DeckType.RESOURCE;
    }

    @Override
    public void reset() {
        resourceDeck.clear();
        resourceDeck.addAll(mappingIdToCard.values());
    }

    @Override
    public Optional<ResourceCard> getCardById(int id) {
        return Optional.ofNullable(mappingIdToCard.getOrDefault(id, null));
    }
}
