package it.polimi.ingsw.am11.model.table;

import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.cards.playable.GoldCard;
import it.polimi.ingsw.am11.model.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.model.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.model.cards.starter.StarterCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.decks.Deck;
import it.polimi.ingsw.am11.model.decks.objective.ObjectiveDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.model.decks.starter.StarterDeckFactory;
import it.polimi.ingsw.am11.model.utils.memento.DeckManagerMemento;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class DeckManager {
    private final @NotNull Deck<GoldCard> goldDeck;
    private final @NotNull Deck<ResourceCard> resourceDeck;
    private final @NotNull Deck<ObjectiveCard> objectiveDeck;
    private final @NotNull Deck<StarterCard> starterDeck;

    public DeckManager() {
        this.goldDeck = GoldDeckFactory.createDeck();
        this.resourceDeck = ResourceDeckFactory.createDeck();
        this.objectiveDeck = ObjectiveDeckFactory.createDeck();
        this.starterDeck = StarterDeckFactory.createDeck();
    }

    /**
     * Method to reset the deck based on its initial idMapping
     */
    public void reset() {
        goldDeck.reset();
        resourceDeck.reset();
        objectiveDeck.reset();
        starterDeck.reset();
    }

    /**
     * Method to shuffle the deck, the shuffle is random
     */
    public void shuffle() {
        goldDeck.shuffle();
        resourceDeck.shuffle();
        objectiveDeck.shuffle();
        starterDeck.shuffle();
    }

    /**
     * Used to get the Color out of the deck, if the deck is empty, it returns an empty Optional
     *
     * @param type the type of the deck
     * @return the color of the top card out of the deck, if present
     */
    public @NotNull Optional<GameColor> getDeckTop(@NotNull PlayableCardType type) {
        return switch (type) {
            case GOLD -> goldDeck.peekTop().map(GoldCard::getColor);
            case RESOURCE -> resourceDeck.peekTop().map(ResourceCard::getColor);
        };
    }

    /**
     * Method to draw a card from a deck of PlayableCardType cards, if the deck is empty, it returns
     * an empty Optional
     *
     * @param type the type of the deck
     * @return the PlayableCard drawn from the deck, if present
     */
    public @NotNull Optional<PlayableCard> drawPlayableFrom(@NotNull PlayableCardType type) {
        return switch (type) {
            case GOLD -> goldDeck.draw().map(PlayableCard.class::cast);
            case RESOURCE -> resourceDeck.draw().map(PlayableCard.class::cast);
        };
    }

    /**
     * Method to draw a card from the starter deck, if the deck is empty, it returns an empty
     * Optional
     *
     * @return the StarterCard drawn from the deck, if present
     */
    public @NotNull Optional<StarterCard> drawStarter() {
        return starterDeck.draw();
    }

    /**
     * Method to draw a card from the objective deck, if the deck is empty, it returns an empty
     *
     * @return the ObjectiveCard drawn from the deck, if present
     */
    public @NotNull Optional<ObjectiveCard> drawObjective() {
        return objectiveDeck.draw();
    }

    /**
     * Method to get the number of remaining cards of a specific PlayableCardType type in the deck
     *
     * @param type the type of the deck
     * @return the number of remaining cards of the specified type
     */
    public int getRemainingCardsOf(@NotNull PlayableCardType type) {
        return switch (type) {
            case GOLD -> goldDeck.getRemainingCards();
            case RESOURCE -> resourceDeck.getRemainingCards();
        };
    }

    /**
     * Method used to save the state of the decks, it saves also the order of the cards in the
     * shuffled decks
     *
     * @return a DeckManagerMemento containing the state of the decks
     */
    public @NotNull DeckManagerMemento save() {
        return new DeckManagerMemento(resourceDeck.save(),
                                      goldDeck.save(),
                                      starterDeck.save(),
                                      objectiveDeck.save());
    }

    /**
     * Method used to load an old configuration of the DeckManager, it loads also the order of the
     * cards in the shuffled decks
     *
     * @param memento the DeckManagerMemento containing the state of the decks
     */
    public void load(@NotNull DeckManagerMemento memento) {
        goldDeck.load(memento.goldDeck());
        resourceDeck.load(memento.resourceDeck());
        objectiveDeck.load(memento.objectiveDeck());
        starterDeck.load(memento.starterDeck());
    }
}
