package it.polimi.ingsw.am11.model.utils.memento;

import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;

public record CardContainerMemento(int card,
                                   @NotNull Map<Corner, Boolean> coveredCorners,
                                   boolean isRetro)
        implements Serializable {
}
