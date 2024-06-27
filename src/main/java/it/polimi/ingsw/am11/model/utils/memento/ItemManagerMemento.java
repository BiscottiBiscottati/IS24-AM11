package it.polimi.ingsw.am11.model.utils.memento;

import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.Symbol;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;

public record ItemManagerMemento(@NotNull Map<GameColor, Integer> exposedColors,
                                 @NotNull Map<Symbol, Integer> exposedSymbols,
                                 @NotNull Map<GameColor, Integer> placedCardColors)
        implements Serializable {

}
