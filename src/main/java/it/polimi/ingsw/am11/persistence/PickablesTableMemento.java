package it.polimi.ingsw.am11.persistence;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public record PickablesTableMemento(@NotNull DeckManagerMemento deckManager,
                                    @NotNull Map<PlayableCardType, Set<Integer>> shownPlayable,
                                    @NotNull Set<Integer> commonObjs)
        implements Serializable {
}
