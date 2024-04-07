package it.polimi.ingsw.am11.decks;

import com.google.common.collect.ImmutableMap;
import it.polimi.ingsw.am11.cards.utils.CardIdentity;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Stack;

/**
 * Represents a deck of cards.
 *
 * @param <T> The type of card that the deck contains.
 */
public class Deck<T extends CardIdentity> {

    private final ImmutableMap<Integer, T> mappingIdToCard;

    private final Stack<T> deck;

    /**
     * Constructor for the Deck class.
     *
     * @param mappingIdToCard An <code>ImmutableMap</code> that maps card IDs to their corresponding card objects
     */
    public Deck(@NotNull ImmutableMap<Integer, T> mappingIdToCard) {
        this.mappingIdToCard = mappingIdToCard;
        this.deck = new Stack<>();
        this.deck.addAll(mappingIdToCard.values());
    }

    /**
     * Shuffles the deck and returns it.
     *
     * @return The deck shuffled.
     */
    public Deck<T> shuffle() {
        Collections.shuffle(this.deck);
        return this;
    }

    /**
     * Method to draw a card from the deck.
     * The card is removed from the deck.
     *
     * @return The card on top of the deck.
     */
    public T drawCard() {
        return this.deck.pop();
    }

    /**
     * Method to get the number of cards remaining in the deck.
     *
     * @return The number of cards remaining in the deck.
     */
    public int getRemainingCards() {
        return this.deck.size();
    }

    /**
     * Method to reset the deck based on its initial idMapping.
     */
    public void reset() {
        this.deck.clear();
        this.deck.addAll(mappingIdToCard.values());
    }

    /**
     * Method to get a card by its id.
     *
     * @param id The id of the card to get.
     * @return The card with the given id, if present.
     */
    Optional<T> getCardById(int id) {
        return Optional.ofNullable(mappingIdToCard.getOrDefault(id, null));
    }
}