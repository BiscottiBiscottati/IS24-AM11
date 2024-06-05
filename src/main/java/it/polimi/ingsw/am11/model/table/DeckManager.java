package it.polimi.ingsw.am11.model.table;

import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.cards.playable.GoldCard;
import it.polimi.ingsw.am11.model.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.model.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.model.cards.starter.StarterCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
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

    public void reset() {
        goldDeck.reset();
        resourceDeck.reset();
        objectiveDeck.reset();
        starterDeck.reset();
    }

    public void shuffle() {
        goldDeck.shuffle();
        resourceDeck.shuffle();
        objectiveDeck.shuffle();
        starterDeck.shuffle();
    }

    public @NotNull Optional<Color> getDeckTop(@NotNull PlayableCardType type) {
        return switch (type) {
            case GOLD -> goldDeck.peekTop().map(GoldCard::getColor);
            case RESOURCE -> resourceDeck.peekTop().map(ResourceCard::getColor);
        };
    }

    public @NotNull Optional<PlayableCard> drawPlayableFrom(@NotNull PlayableCardType type) {
        return switch (type) {
            case GOLD -> goldDeck.draw().map(PlayableCard.class::cast);
            case RESOURCE -> resourceDeck.draw().map(PlayableCard.class::cast);
        };
    }

    public @NotNull Optional<StarterCard> drawStarter() {
        return starterDeck.draw();
    }

    public @NotNull Optional<ObjectiveCard> drawObjective() {
        return objectiveDeck.draw();
    }

    public int getNumberRemainingOf(@NotNull PlayableCardType type) {
        return switch (type) {
            case GOLD -> goldDeck.getRemainingCards();
            case RESOURCE -> resourceDeck.getRemainingCards();
        };
    }

    public @NotNull DeckManagerMemento save() {
        return new DeckManagerMemento(resourceDeck.save(),
                                      goldDeck.save(),
                                      starterDeck.save(),
                                      objectiveDeck.save());
    }

    public void load(@NotNull DeckManagerMemento memento) {
        goldDeck.load(memento.goldDeck());
        resourceDeck.load(memento.resourceDeck());
        objectiveDeck.load(memento.objectiveDeck());
        starterDeck.load(memento.starterDeck());
    }
}
