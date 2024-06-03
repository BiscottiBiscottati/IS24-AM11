package it.polimi.ingsw.am11.persistence;

import it.polimi.ingsw.am11.model.utils.TurnAction;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Set;

public record PlayerManagerMemento(@NotNull Set<PlayerMemento> players,
                                   @NotNull String firstPlayer,
                                   @NotNull String currentPlayer,
                                   @NotNull TurnAction currentAction)
        implements Serializable {
}
