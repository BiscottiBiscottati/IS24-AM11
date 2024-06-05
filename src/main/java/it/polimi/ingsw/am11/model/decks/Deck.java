package it.polimi.ingsw.am11.model.decks;

import com.google.common.collect.ImmutableMap;
import it.polimi.ingsw.am11.model.cards.utils.CardIdentity;
import it.polimi.ingsw.am11.model.decks.objective.ObjectiveDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.model.decks.starter.StarterDeckFactory;
import it.polimi.ingsw.am11.model.utils.memento.DeckMemento;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
 * GOLD: {@link GoldDeckFactory GoldDeckFactory}
 * RESOURCE: {@link ResourceDeckFactory ResourceDeckFactory}
 * STARTER: {@link StarterDeckFactory StarterDeckFactory}
 * OBJECTIVE: {@link ObjectiveDeckFactory ObjectiveDeckFactory}
 * </pre>
 *
 * @param <T> The type of card that the deck contains.
 */
public class Deck<T extends CardIdentity> {

    private final @NotNull ImmutableMap<Integer, T> mappingIdToCard;

    private final @NotNull ArrayDeque<T> deck;

    /**
     * Constructor for the Deck class.
     *
     * @param mappingIdToCard An <code>ImmutableMap</code> that maps card IDs to their corresponding
     *                        card objects
     */
    public Deck(@NotNull ImmutableMap<Integer, T> mappingIdToCard) {
        this.mappingIdToCard = mappingIdToCard;
        this.deck = new ArrayDeque<>(mappingIdToCard.values());
    }

    /**
     * Shuffles the deck and returns it.
     *
     * @return The deck shuffled.
     */
    public @NotNull Deck<T> shuffle() {
        List<T> tempList = new ArrayList<>(this.deck.stream().toList());
        Collections.shuffle(tempList);
        this.deck.clear();
        this.deck.addAll(tempList);
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
        return Optional.of(this.deck.pollFirst());
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

    public @NotNull DeckMemento save() {
        List<Integer> current = this.deck.stream()
                                         .map(CardIdentity::getId)
                                         .toList();
        return new DeckMemento(current);
    }

    public void load(@NotNull DeckMemento memento) {
        this.deck.clear();
        this.deck.addAll(memento.cards().stream().map(mappingIdToCard::get).toList());
    }

    /**
     * Method to get a card by its id.
     *
     * @param id The id of the card to get.
     * @return The card with the given id, if present.
     */
    public @NotNull Optional<T> getCardById(int id) {
        return Optional.ofNullable(mappingIdToCard.getOrDefault(id, null));
    }

    public @NotNull Optional<T> peekTop() {
        if (deck.isEmpty()) return Optional.empty();
        return Optional.of(deck.peek());
    }
}