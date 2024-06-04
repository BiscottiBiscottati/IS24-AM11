package it.polimi.ingsw.am11.persistence.memento;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public record FieldMemento(@NotNull ItemManagerMemento exposedItems,
                           @NotNull PositionManagerMemento positionManager)
        implements Serializable {
}
