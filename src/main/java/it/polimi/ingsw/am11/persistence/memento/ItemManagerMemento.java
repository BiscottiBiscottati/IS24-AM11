package it.polimi.ingsw.am11.persistence.memento;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.Symbol;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;

public record ItemManagerMemento(@NotNull Map<Color, Integer> exposedColors,
                                 @NotNull Map<Symbol, Integer> exposedSymbols,
                                 @NotNull Map<Color, Integer> placedCardColors)
        implements Serializable {

}
