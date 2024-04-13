package it.polimi.ingsw.am11.decks;

import com.google.common.collect.ImmutableMap;
import it.polimi.ingsw.am11.cards.utils.CardIdentity;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Stack;

/**
 * Represents a deck of cards.
 * <p>
 * Can be shuffled and cards can be drawn from it.
 * <p>
 * For each type of <code>Deck</code> you need to use the specific static method
 * <code>createDeck</code> of the factory class.
 * <p>
 * The available factories are:
 * <pre>
 * GOLD: {@link it.polimi.ingsw.am11.decks.playable.GoldDeckFactory GoldDeckFactory}
 * RESOURCE: {@link it.polimi.ingsw.am11.decks.playable.ResourceDeckFactory ResourceDeckFactory}
 * STARTER: {@link it.polimi.ingsw.am11.decks.starter.StarterDeckFactory StarterDeckFactory}
 * OBJECTIVE: {@link it.polimi.ingsw.am11.decks.objective.ObjectiveDeckFactory ObjectiveDeckFactory}
 * </pre>
 *
 * @param <T> The type of card that the deck contains.
 */
public class Deck<T extends CardIdentity> {

    private final ImmutableMap<Integer, T> mappingIdToCard;

    private final Stack<T> deck;

    /**
     * Constructor for the Deck class.
     *
     * @param mappingIdToCard An <code>ImmutableMap</code> that maps card IDs to their corresponding
     *                        card objects
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
     * Method to draw a card from the deck. The card is removed from the deck.
     * <p>
     * If the deck is empty, it will return an empty Optional.
     *
     * @return The card on top of the deck as an <code>Optional</code>.
     */
    @NotNull
    public Optional<T> draw() {
        if (this.deck.isEmpty()) return Optional.empty();
        return Optional.of(this.deck.pop());
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
    public Optional<T> getCardById(int id) {
        return Optional.ofNullable(mappingIdToCard.getOrDefault(id, null));
    }

    public Optional<T> peekTop() {
        if (deck.isEmpty()) return Optional.empty();
        return Optional.of(deck.peek());
    }
}