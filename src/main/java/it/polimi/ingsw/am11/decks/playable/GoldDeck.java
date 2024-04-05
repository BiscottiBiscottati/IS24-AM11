package it.polimi.ingsw.am11.decks.playable;

import com.google.common.collect.ImmutableMap;
import it.polimi.ingsw.am11.cards.playable.GoldCard;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.utils.DeckType;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Stack;

public class GoldDeck implements Deck<GoldCard> {

    private final ImmutableMap<Integer, GoldCard> mappingIdToCard;
    private final Stack<GoldCard> goldDeck;

    public GoldDeck(@NotNull ImmutableMap<Integer, GoldCard> mappingIdToCard) {
        this.mappingIdToCard = mappingIdToCard;
        this.goldDeck = new Stack<>();
        this.goldDeck.addAll(mappingIdToCard.values());
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
        return DeckType.GOLD;
    }

    @Override
    public void reset() {
        goldDeck.clear();
        goldDeck.addAll(mappingIdToCard.values());
    }

    @Override
    public Optional<GoldCard> getCardById(int id) {
        return Optional.ofNullable(mappingIdToCard.getOrDefault(id, null));
    }
}
