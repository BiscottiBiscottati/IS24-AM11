package it.polimi.ingsw.am11.model.utils.memento;

import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public record ReconnectionTableMemento
        (@NotNull Map<PlayableCardType, @Nullable GameColor> deckTops,
         @NotNull Map<PlayableCardType, Set<@Nullable Integer>> shownPlayable,
         @NotNull Set<@NotNull Integer> commonObjs)
        implements Serializable {
}
