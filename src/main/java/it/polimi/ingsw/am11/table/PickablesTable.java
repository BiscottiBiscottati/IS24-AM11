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
import it.polimi.ingsw.am11.decks.utils.DeckType;
import it.polimi.ingsw.am11.exceptions.EmptyDeckException;
import it.polimi.ingsw.am11.exceptions.IllegalPickActionException;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PickablesTable {

    private final Deck<GoldCard> goldDeck;
    private final Deck<ResourceCard> resourceDeck;
    private final Deck<ObjectiveCard> objectiveDeck;
    private final Deck<StarterCard> starterDeck;
    private final int numOfObjectives;
    private final int numOfShownPerType;
    private final List<ObjectiveCard> commonObjectives;
    private final List<PlayableCard> shownGold;
    private final List<PlayableCard> shownResources;

    public PickablesTable(int numOfObjectives, int numOfShownPerType) {
        this.goldDeck = GoldDeckFactory.createDeck();
        this.resourceDeck = ResourceDeckFactory.createDeck();
        this.objectiveDeck = ObjectiveDeckFactory.createDeck();
        this.starterDeck = StarterDeckFactory.createDeck();
        this.numOfObjectives = numOfObjectives;
        this.numOfShownPerType = numOfShownPerType;

        this.commonObjectives = new ArrayList<>(numOfObjectives);
        this.shownGold = new ArrayList<>(numOfShownPerType);
        this.shownResources = new ArrayList<>(numOfShownPerType);

    }

    //region Getters

    public List<ObjectiveCard> getCommonObjectives() {
        return Collections.unmodifiableList(commonObjectives);
    }

    public List<PlayableCard> getShownGold() {
        return Collections.unmodifiableList(shownGold);
    }

    public List<PlayableCard> getShownResources() {
        return Collections.unmodifiableList(shownResources);
    }

    public Optional<Color> getResourceDeckTop() {
        if (resourceDeck.peekTop().isPresent()) {
            return Optional.of(resourceDeck.peekTop().get().getColor());
        } else {
            return Optional.empty();
        }
    }

    public Optional<Color> getGoldDeckTop() {
        if (goldDeck.peekTop().isPresent()) {
            return Optional.of(goldDeck.peekTop().get().getColor());
        } else {
            return Optional.empty();
        }
    }

    public Optional<PlayableCard> getPlayableByID(int id) {
        Optional<ResourceCard> temp = resourceDeck.getCardById(id);
        if (temp.isPresent()) {
            return Optional.of(temp.get());
        } else if (goldDeck.getCardById(id).isPresent()) {
            Optional<GoldCard> cardById = goldDeck.getCardById(id);
            return Optional.of(cardById.get());
        } else {
            return Optional.empty();
        }
    }

    public Optional<StarterCard> getStarterByID(int id) {
        return starterDeck.getCardById(id);
    }

    public Optional<ObjectiveCard> getObjectiveByID(int id) {
        return objectiveDeck.getCardById(id);
    }

    //endregion
    public void addDeck(DeckType type, Deck deck) {

    }

    public void initialize() {

    }

    public void shuffleAllDecks() {
        goldDeck.shuffle();
        resourceDeck.shuffle();
        objectiveDeck.shuffle();
        starterDeck.shuffle();
    }

    public @Nullable PlayableCard pickPlayableCardFrom(PlayableCardType type) throws EmptyDeckException {
        return switch (type) {
            case GOLD -> goldDeck.draw().orElseThrow(() -> new EmptyDeckException("Gold deck is empty!"));
            case RESOURCE -> resourceDeck.draw().orElseThrow(() -> new EmptyDeckException("Resource deck is empty!"));
            default -> null;
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

    public void pickCommonObjectives() {
        for (int i = 0; i < numOfObjectives; i++) {
            commonObjectives.add(objectiveDeck.draw().get());
        }
    }

    public void resetToInitialCondition() {
        //TODO
        //It has to prepare the table for a new game, so it has to make sure that each
        //deck is complete.
    }

    public PlayableCard pickGoldVisible(int ID) throws IllegalPickActionException {
        for (PlayableCard card : shownGold) {
            if (card.getId() == ID) {
                shownGold.remove(card);
                supplyGoldVisibles();
                return card;
            }
        }
        throw new IllegalPickActionException("Gold card with ID " + ID + " is not visible!");
    }

    public PlayableCard pickResourceVisibles(int ID) throws IllegalPickActionException {
        for (PlayableCard card : shownResources) {
            if (card.getId() == ID) {
                shownResources.remove(card);
                supplyResourceVisibles();
                return card;
            }
        }
        throw new IllegalPickActionException("Resource card with ID " + ID + " is not visible!");
    }

    public Optional<PlayableCard> supplyGoldVisibles() throws IllegalPickActionException {
        if (shownGold.size() >= numOfShownPerType) {
            throw new IllegalPickActionException("There are already enough shown gold cards!");
        }
        Optional<GoldCard> drawnCard = goldDeck.draw();
        if (drawnCard.isPresent()) {
            shownGold.add(drawnCard.get());
            return Optional.of(drawnCard.get());
        } else {
            return Optional.empty();
        }
    }

    public Optional<PlayableCard> supplyResourceVisibles() throws IllegalPickActionException {
        if (shownResources.size() >= numOfShownPerType) {
            throw new IllegalPickActionException("There are already enough shown resource cards!");
        }
        Optional<ResourceCard> drawnCard = resourceDeck.draw();
        if (drawnCard.isPresent()) {
            shownResources.add(drawnCard.get());
            return Optional.of(drawnCard.get());
        } else {
            return Optional.empty();
        }
    }

}
