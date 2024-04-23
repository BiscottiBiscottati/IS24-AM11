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
import it.polimi.ingsw.am11.model.exceptions.EmptyDeckException;
import it.polimi.ingsw.am11.model.exceptions.IllegalPickActionException;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class PickablesTable {

    private static int numOfObjectives;
    private static int numOfShownPerType;
    private final Deck<GoldCard> goldDeck;
    private final Deck<ResourceCard> resourceDeck;
    private final Deck<ObjectiveCard> objectiveDeck;
    private final Deck<StarterCard> starterDeck;
    private final Set<ObjectiveCard> commonObjectives;
    private final Set<GoldCard> shownGold;
    private final Set<ResourceCard> shownResources;

    public PickablesTable() {
        this.goldDeck = GoldDeckFactory.createDeck();
        this.resourceDeck = ResourceDeckFactory.createDeck();
        this.objectiveDeck = ObjectiveDeckFactory.createDeck();
        this.starterDeck = StarterDeckFactory.createDeck();

        this.commonObjectives = new HashSet<>(numOfObjectives << 1);
        this.shownGold = new HashSet<>(numOfShownPerType << 1);
        this.shownResources = new HashSet<>(numOfShownPerType << 1);

    }

    public static void setNumOfObjectives(int numOfObjectives) {
        PickablesTable.numOfObjectives = numOfObjectives;
    }

    public static void setNumOfShownPerType(int numOfShownPerType) {
        PickablesTable.numOfShownPerType = numOfShownPerType;
    }

    //region Getters

    public Set<ObjectiveCard> getCommonObjectives() {
        return Collections.unmodifiableSet(commonObjectives);
    }

    public Optional<Color> getDeckTop(@NotNull PlayableCardType type) {
        return switch (type) {
            case GOLD -> goldDeck.peekTop()
                                 .map(PlayableCard::getColor);
            case RESOURCE -> resourceDeck.peekTop()
                                         .map(PlayableCard::getColor);
        };
    }

    public Optional<PlayableCard> getPlayableByID(int id) {
        return resourceDeck.getCardById(id)
                           .map(PlayableCard.class::cast)
                           .or(() -> goldDeck.getCardById(id));
    }

    public Optional<StarterCard> getStarterByID(int id) {
        return starterDeck.getCardById(id);
    }

    public Optional<ObjectiveCard> getObjectiveByID(int id) {
        return objectiveDeck.getCardById(id);
    }

    public @NotNull PlayableCard drawPlayableFrom(@NotNull PlayableCardType type)
    throws EmptyDeckException {
        return switch (type) {
            case GOLD -> goldDeck.draw().orElseThrow(
                    () -> new EmptyDeckException("Gold deck is empty!"));
            case RESOURCE -> resourceDeck.draw().orElseThrow(
                    () -> new EmptyDeckException("Resource deck is empty!"));
        };
    }

    public StarterCard pickStarterCard()
    throws EmptyDeckException {
        return starterDeck.draw()
                          .orElseThrow(() -> new EmptyDeckException("Starter deck is empty!"));
    }

    public ObjectiveCard pickObjectiveCard() throws EmptyDeckException {
        return objectiveDeck.draw()
                            .orElseThrow(() -> new EmptyDeckException("Objective deck is empty!"));

    }

    public void initialize() {
        commonObjectives.clear();
        shownGold.clear();
        shownResources.clear();
        resetDecks();
        shuffleDecks();
        try {
            pickCommonObjectives();
        } catch (EmptyDeckException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < numOfShownPerType; i++) {
            goldDeck.draw().ifPresent(shownGold::add);
            resourceDeck.draw().ifPresent(shownResources::add);
        }
    }

    private void resetDecks() {
        goldDeck.reset();
        resourceDeck.reset();
        objectiveDeck.reset();
        starterDeck.reset();
    }

    private void shuffleDecks() {
        goldDeck.shuffle();
        resourceDeck.shuffle();
        objectiveDeck.shuffle();
        starterDeck.shuffle();
    }

    public void pickCommonObjectives() throws EmptyDeckException {
        for (int i = 0; i < numOfObjectives; i++) {
            commonObjectives.add(objectiveDeck.draw()
                                              .orElseThrow(() -> new EmptyDeckException(
                                                      "Objective deck is empty!")));
        }
    }

    public PlayableCard pickPlayableVisible(int cardID) throws IllegalPickActionException {
        return Stream.concat(shownGold.stream(), shownResources.stream())
                     .filter(card -> card.getId() == cardID)
                     .findFirst()
                     .map(this::removePlayable)
                     .map(playableCard -> {
                         supplyVisible();
                         return playableCard;
                     })
                     .orElseThrow(() -> new IllegalPickActionException(
                             "Card with ID " + cardID + " is not visible!"));
    }

    private @NotNull PlayableCard removePlayable(@NotNull PlayableCard playableCard) {
        switch (playableCard) {
            case GoldCard goldCard -> {
                shownGold.remove(goldCard);
                return goldCard;
            }
            case ResourceCard resourceCard -> {
                shownResources.remove(resourceCard);
                return resourceCard;
            }
        }
    }

    private void supplyVisible() {
        if (shownGold.size() < numOfShownPerType) {
            goldDeck.draw().ifPresent(shownGold::add);
        }
        if (shownResources.size() < numOfShownPerType) {
            resourceDeck.draw().ifPresent(shownResources::add);
        }
    }

    public Set<PlayableCard> getShownPlayable(@NotNull PlayableCardType type) {
        return switch (type) {
            case GOLD -> Set.copyOf(shownGold);
            case RESOURCE -> Set.copyOf(shownResources);
        };
    }

    public int getRemainingDeckOf(@NotNull PlayableCardType type) {
        return switch (type) {
            case GOLD -> goldDeck.getRemainingCards();
            case RESOURCE -> resourceDeck.getRemainingCards();
        };
    }

}
