package it.polimi.ingsw.am11.table;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.utils.DeckType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class PickablesTable {

    private final EnumMap<DeckType, Deck> decks;
    private final int numOfObjectives;
    private final int numOfShownPerType;
    private final List<ObjectiveCard> commonObjectives;

    public PickablesTable(int numOfObjectives, int numOfShownPerType) {
        this.numOfObjectives = numOfObjectives;
        this.numOfShownPerType = numOfShownPerType;
        //TODO: initialize decks
        this.decks = new EnumMap<>(DeckType.class);
        this.commonObjectives = new ArrayList<ObjectiveCard>();
    }

    public List<ObjectiveCard> getCommonObjectives() {
        return commonObjectives;
    }

    public List<PlayableCard> getShownCards() {
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
}
