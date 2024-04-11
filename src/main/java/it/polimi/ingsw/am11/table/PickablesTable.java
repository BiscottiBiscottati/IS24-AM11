package it.polimi.ingsw.am11.table;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.utils.CardIdentity;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.objective.ObjectiveDeckFactory;
import it.polimi.ingsw.am11.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.decks.starter.StarterDeckFactory;
import it.polimi.ingsw.am11.decks.utils.DeckType;
import it.polimi.ingsw.am11.exceptions.IllegalPickActionException;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

public class PickablesTable {

    private final EnumMap<DeckType, Deck> decks;
    private final int numOfObjectives;
    private final int numOfShownPerType;
    private final List<ObjectiveCard> commonObjectives;
    private final List<PlayableCard> shownGold;
    private final List<PlayableCard> shownResources;

    public PickablesTable(int numOfObjectives, int numOfShownPerType) {
        this.numOfObjectives = numOfObjectives;
        this.numOfShownPerType = numOfShownPerType;
        //TODO: initialize decks
        this.decks = new EnumMap<>(DeckType.class);
        this.commonObjectives = new ArrayList<>(2);
        this.shownGold = new ArrayList<>(2);
        this.shownResources = new ArrayList<>(2);
        this.decks.put(DeckType.OBJECTIVE, ObjectiveDeckFactory.createDeck());
        this.decks.put(DeckType.GOLD, GoldDeckFactory.createDeck());
        this.decks.put(DeckType.RESOURCE, GoldDeckFactory.createDeck());
        this.decks.put(DeckType.STARTER, StarterDeckFactory.createDeck());
    }

    //region Getters

    public List<ObjectiveCard> getCommonObjectives() {
        return commonObjectives;
    }

    public List<PlayableCard> getShownGold() {
        //TODO
        return null;
    }

    public List<PlayableCard> getShownResources() {
        //TODO
        return null;
    }

    public Color getResourceDeckTop() {
        //TODO
        return null;
    }

    public Color getGoldDeckTop() {
        //TODO
        return null;
    }

    public Optional<PlayableCard> getPlayableByID(int id) {
        Optional<PlayableCard> temp = decks.get(DeckType.RESOURCE).getCardById(id);
        if (temp.isPresent()) {
            return temp;
        } else {
            return decks.get(DeckType.GOLD).getCardById(id);
        }
    }

    public Optional<StarterCard> getStarterByID(int id) {
        return decks.get(DeckType.STARTER).getCardById(id);
    }

    public Optional<ObjectiveCard> getObjectiveByID(int id) {
        return decks.get(DeckType.OBJECTIVE).getCardById(id);
    }

    //endregion
    public void addDeck(DeckType type, Deck deck) {
        //TODO
    }

    public void shuffleAllDecks() {
        //TODO
    }

    public CardIdentity pickCardFrom(DeckType deck) {
        //TODO
        return null;
    }

    public void pickCommonObjectives() {
        //TODO, it has to check that the number is equal to numOFOBjectives
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
