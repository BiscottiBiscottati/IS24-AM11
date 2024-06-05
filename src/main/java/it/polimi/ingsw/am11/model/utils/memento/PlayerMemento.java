package it.polimi.ingsw.am11.model.utils.memento;

import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public record PlayerMemento(@NotNull String nickname,
                            @NotNull PlayerColor color,
                            @NotNull PersonalSpaceMemento space,
                            @NotNull FieldMemento field)
        implements Serializable {
}
