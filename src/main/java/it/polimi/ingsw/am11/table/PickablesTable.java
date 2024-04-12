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
import it.polimi.ingsw.am11.exceptions.IllegalPickActionException;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
        return resourceDeck.peekTopCardColor();
    }

    public Optional getGoldDeckTop() {
        return goldDeck.peekTopCardColor();
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
        //TODO
    }

    public void shuffleAllDecks() {
        //TODO
    }

    public Optional<PlayableCard> pickPlayableCardFrom(PlayableCardType type) {
        if (type == PlayableCardType.GOLD) {
            Optional<GoldCard> card = goldDeck.draw();
            return card.map(PlayableCard.class::cast);
        } else {
            Optional<ResourceCard> card = resourceDeck.draw();
            return card.map(PlayableCard.class::cast);
        }
    }

    public Optional<StarterCard> pickStarterCard() {
        return starterDeck.draw();
    }

    public Optional<ObjectiveCard> pickObjectiveCard() {
        return objectiveDeck.draw();
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
        //TODO
        return null;
    }

    public PlayableCard pickResourceVisibles(int ID) throws IllegalPickActionException {
        //TODO
        return null;
    }

    public Optional<PlayableCard> supplyGoldVisibles() throws IllegalPickActionException {
        //TODO, it should return null when the deck is empty, while if there are already enough shown cards it
        //trows an exception
        return null;
    }

    public Optional<PlayableCard> supplyResourceVisibles() throws IllegalPickActionException {
        //TODO, it should return null when the deck is empty, while if there are already enough shown cards it
        //trows an exception
        return null;
    }

}
