package it.polimi.ingsw.am11.view.client.miniModel;

import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.model.utils.memento.PlateauMemento;
import it.polimi.ingsw.am11.model.utils.memento.ReconnectionTableMemento;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CliTable {

    private final @NotNull Map<PlayableCardType, GameColor> deckTopColors;
    private final @NotNull Set<Integer> shownCards;
    private final @NotNull Set<Integer> commonObjectives;
    private @NotNull GameStatus status;

    public CliTable() {
        this.deckTopColors = new EnumMap<>(PlayableCardType.class);
        this.shownCards = new HashSet<>(8);
        this.commonObjectives = new HashSet<>(4);
        this.status = GameStatus.SETUP;
    }

    public @NotNull Set<Integer> getCommonObjectives() {
        return Set.copyOf(commonObjectives);
    }

    public void addCommonObjectives(int cardId) {
        commonObjectives.add(cardId);
    }

    public void removeCommonObjective(int cardId) {
        commonObjectives.remove(cardId);
    }

    public @NotNull GameStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull GameStatus status) {
        this.status = status;
    }

    public GameColor getDeckTop(PlayableCardType type) {
        return deckTopColors.get(type);
    }

    public @NotNull Set<Integer> getShownCards() {
        return Set.copyOf(shownCards);
    }

    public void pickVisible(int cardId) {
        shownCards.remove(cardId);
    }

    public void addVisible(int cardId) {
        shownCards.add(cardId);
    }

    public void refreshDeckTop(@NotNull PlayableCardType type, @Nullable GameColor color) {
        deckTopColors.put(type, color);
    }

    public void load(@NotNull ReconnectionTableMemento memento,
                     @NotNull PlateauMemento plateauMemento) {
        deckTopColors.clear();
        deckTopColors.putAll(memento.deckTops());
        shownCards.clear();
        memento.shownPlayable().values().forEach(shownCards::addAll);
        commonObjectives.clear();
        commonObjectives.addAll(memento.commonObjs());
        status = plateauMemento.status();
    }
}
