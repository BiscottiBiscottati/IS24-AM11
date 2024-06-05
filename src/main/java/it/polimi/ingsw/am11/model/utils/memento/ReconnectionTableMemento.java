package it.polimi.ingsw.am11.model.utils.memento;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public record ReconnectionTableMemento(@NotNull Map<PlayableCardType, Optional<Color>> deckTops,
                                       @NotNull Map<PlayableCardType, Set<Integer>> shownPlayable,
                                       @NotNull Set<Integer> commonObjs)
        implements Serializable {
}
