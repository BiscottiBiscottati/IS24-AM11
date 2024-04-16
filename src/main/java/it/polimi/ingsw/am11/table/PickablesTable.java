package it.polimi.ingsw.am11.table;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.GoldCard;
import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.objective.ObjectiveDeckFactory;
import it.polimi.ingsw.am11.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.decks.starter.StarterDeckFactory;
import it.polimi.ingsw.am11.exceptions.EmptyDeckException;
import it.polimi.ingsw.am11.exceptions.IllegalPickActionException;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public class PickablesTable {

    private static int numOfObjectives;
    private static int numOfShownPerType;
    private final Deck<GoldCard> goldDeck;
    private final Deck<ResourceCard> resourceDeck;
    private final Deck<ObjectiveCard> objectiveDeck;
    private final Deck<StarterCard> starterDeck;
    private final List<ObjectiveCard> commonObjectives;
    private final List<GoldCard> shownGold;
    private final List<ResourceCard> shownResources;

    public PickablesTable() {
        this.goldDeck = GoldDeckFactory.createDeck();
        this.resourceDeck = ResourceDeckFactory.createDeck();
        this.objectiveDeck = ObjectiveDeckFactory.createDeck();
        this.starterDeck = StarterDeckFactory.createDeck();

        this.commonObjectives = new ArrayList<>(numOfObjectives);
        this.shownGold = new ArrayList<>(numOfShownPerType);
        this.shownResources = new ArrayList<>(numOfShownPerType);

    }

    public static void setNumOfObjectives(int numOfObjectives) {
        PickablesTable.numOfObjectives = numOfObjectives;
    }

    public static void setNumOfShownPerType(int numOfShownPerType) {
        PickablesTable.numOfShownPerType = numOfShownPerType;
    }

    //region Getters

    public List<ObjectiveCard> getCommonObjectives() {
        return Collections.unmodifiableList(commonObjectives);
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

    public List<PlayableCard> getShownPlayable() {
        return Stream.concat(shownGold.stream(), shownResources.stream())
                     .toList();
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
            case GOLD -> new HashSet<>(shownGold);
            case RESOURCE -> new HashSet<>(shownResources);
        };
    }

    public int getRemainingDeckOf(@NotNull PlayableCardType type) {
        return switch (type) {
            case GOLD -> goldDeck.getRemainingCards();
            case RESOURCE -> resourceDeck.getRemainingCards();
        };
    }

}
