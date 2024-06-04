package it.polimi.ingsw.am11.persistence.memento;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Set;

public record PersonalSpaceMemento(@NotNull Set<Integer> hand,
                                   @NotNull Set<Integer> personalObjs,
                                   @NotNull Set<Integer> candidateObjs,
                                   int starterCard)
        implements Serializable {
}
