package it.polimi.ingsw.am11.decks.playable;

import com.google.common.collect.ImmutableMap;
import it.polimi.ingsw.am11.cards.playable.GoldCard;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.utils.DeckType;
import it.polimi.ingsw.am11.decks.utils.PlayableDeckType;

import java.util.ArrayList;
import java.util.Optional;

public class GoldDeck implements Deck<GoldCard> {

    private final ImmutableMap<Integer, GoldCard> mappingIdToCard;
    private ArrayList<GoldCard> goldDeck;

    public GoldDeck(ImmutableMap<Integer, GoldCard> mappingIdToCard) {
        this.mappingIdToCard = mappingIdToCard;
    }

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

    @Override
    public void reset() {

    }

    @Override
    public Optional<GoldCard> getCardById(int id) {
        return Optional.ofNullable(mappingIdToCard.getOrDefault(id, null));
    }
}
