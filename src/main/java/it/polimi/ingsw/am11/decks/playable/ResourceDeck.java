package it.polimi.ingsw.am11.decks.playable;

import com.google.common.collect.ImmutableMap;
import it.polimi.ingsw.am11.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.utils.DeckType;
import it.polimi.ingsw.am11.decks.utils.PlayableDeckType;

import java.util.ArrayList;
import java.util.Optional;

public class ResourceDeck implements Deck<ResourceCard> {

    private final ImmutableMap<Integer, ResourceCard> mappingIdToCard;
    private ArrayList<ResourceCard> resourceDeck;

    public ResourceDeck(ImmutableMap<Integer, ResourceCard> mappingIdToCard) {
        this.mappingIdToCard = mappingIdToCard;
    }

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

    @Override
    public void reset() {

    }

    @Override
    public Optional<ResourceCard> getCardById(int id) {
        return Optional.ofNullable(mappingIdToCard.getOrDefault(id, null));
    }
}
