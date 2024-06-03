package it.polimi.ingsw.am11.persistence;

import it.polimi.ingsw.am11.model.players.utils.Position;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public record FieldMemento(@NotNull Set<Position> availablePos,
                           @NotNull Set<Position> closedPos,
                           @NotNull Map<Position, CardContainerMemento> cardPositioned)
        implements Serializable {
}
