package it.polimi.ingsw.am11.decks.objective;

import com.google.common.collect.ImmutableMap;
import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.utils.DeckType;

import java.util.Optional;
import java.util.Stack;

public class ObjectiveDeck implements Deck<ObjectiveCard> {

    private final ImmutableMap<Integer, ObjectiveCard> mappingIdToCard;
    private Stack<ObjectiveCard> objectiveDeck;

    public ObjectiveDeck(ImmutableMap<Integer, ObjectiveCard> mappingIdToCard) {
        this.mappingIdToCard = mappingIdToCard;
    }

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
        return DeckType.OBJECTIVE;
    }

    @Override
    public void reset() {

    }

    @Override
    public Optional<ObjectiveCard> getCardById(int id) {
        return Optional.ofNullable(mappingIdToCard.getOrDefault(id, null));
    }
}
