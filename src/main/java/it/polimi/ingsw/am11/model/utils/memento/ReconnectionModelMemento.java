package it.polimi.ingsw.am11.model.utils.memento;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public record ReconnectionModelMemento(@NotNull ReconnectionTableMemento table,
                                       @NotNull PlateauMemento plateau,
                                       @NotNull PlayerManagerMemento playerManager)
        implements Serializable {
}
