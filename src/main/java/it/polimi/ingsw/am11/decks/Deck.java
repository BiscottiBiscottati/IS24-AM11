package it.polimi.ingsw.am11.decks;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Stack;

public class Deck<T> {
    private final ImmutableMap<Integer, T> mappingIdToCard;

    private final Stack<T> deck;

    public Deck(@NotNull ImmutableMap<Integer, T> mappingIdToCard) {
        this.mappingIdToCard = mappingIdToCard;
        this.deck = new Stack<>();
        this.deck.addAll(mappingIdToCard.values());
    }


    public Deck<T> shuffle() {
        Collections.shuffle(this.deck);
        return this;
    }

    public T drawCard() {
        return this.deck.pop();
    }

    public int getRemainingCards() {
        return this.deck.size();
    }

    public void addCard(T card) {
        this.deck.push(card);
    }

    public void reset() {
        this.deck.clear();
        this.deck.addAll(mappingIdToCard.values());
    }

    Optional<T> getCardById(int id) {
        return Optional.ofNullable(mappingIdToCard.getOrDefault(id, null));
    }
}
