package it.polimi.ingsw.am11.persistence;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.Symbol;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.EnumMap;

public record ExposedItemMemento(@NotNull EnumMap<Color, Integer> exposedColors,
                                 @NotNull EnumMap<Symbol, Integer> exposedSymbols,
                                 @NotNull EnumMap<Color, Integer> placedCardColors)
        implements Serializable {

}
