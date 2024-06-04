package it.polimi.ingsw.am11.persistence.memento;

import it.polimi.ingsw.am11.model.table.GameStatus;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;

public record PlateauMemento(@NotNull Map<String, Integer> playerPoints,
                             @NotNull Map<String, Integer> objCounter,
                             @NotNull Map<String, Integer> leaderboard,
                             @NotNull GameStatus status)
        implements Serializable {
}
