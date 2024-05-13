package it.polimi.ingsw.am11.view.client.miniModel;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.table.GameStatus;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

public class CliTable {

    private final EnumMap<PlayableCardType, Color> deckTopColors;
    private final Set<Integer> shownCards;
    private final Set<Integer> commonObjectives;
    private GameStatus status;

    public CliTable() {
        this.deckTopColors = new EnumMap<>(PlayableCardType.class);
        this.shownCards = new HashSet<>();
        this.commonObjectives = new HashSet<>();
    }

    public Set<Integer> getCommonObjectives() {
        return new HashSet<>(commonObjectives);
    }

    public void addCommonObjectives(int cardId) {
        commonObjectives.add(cardId);
    }

    public void removeCommonObjective(int cardId) {
        commonObjectives.remove(cardId);
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public Color getDeckTop(PlayableCardType type) {
        return deckTopColors.get(type);
    }

    public Set<Integer> getShownCards() {
        return new HashSet<>(shownCards);
    }

    public void pickVisible(int cardId) {
        shownCards.remove(cardId);
    }

    public void addVisible(int cardId) {
        shownCards.add(cardId);
    }

    public void refreshDeckTop(PlayableCardType type, Color color) {
        deckTopColors.put(type, color);
    }
}
