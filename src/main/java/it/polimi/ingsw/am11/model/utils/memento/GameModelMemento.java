package it.polimi.ingsw.am11.model.utils.memento;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public record GameModelMemento(@NotNull PickablesTableMemento table,
                               @NotNull PlateauMemento plateau,
                               @NotNull PlayerManagerMemento playerManager)
        implements Serializable {
}
