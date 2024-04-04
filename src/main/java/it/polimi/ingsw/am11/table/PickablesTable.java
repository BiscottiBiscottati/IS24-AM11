package it.polimi.ingsw.am11.table;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.utils.DeckType;

import java.util.ArrayList;
import java.util.EnumMap;

public class PickablesTable {

    private final EnumMap<DeckType, Deck> decks;
    private final int numOfObjectives;
    private final int numOfShownPerType;
    private ArrayList<ObjectiveCard> commonObjectives;

    public PickablesTable(int numOfObjectives, int numOfShownPerType) {
        this.numOfObjectives = numOfObjectives;
        this.numOfShownPerType = numOfShownPerType;
        //TODO: initialize decks
        this.decks = new EnumMap<>(DeckType.class);
    }
}
