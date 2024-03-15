package it.polimi.ingsw.am11.cards.objective;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.cards.utils.Color;
import it.polimi.ingsw.am11.cards.utils.Symbol;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public abstract class PositioningCard extends ObjectiveCard {
    private static final ImmutableMap<Symbol, Integer> SYMBOL_REQUIREMENTS = Maps.immutableEnumMap(
            ImmutableMap.of(
                    Symbol.FEATHER, 0,
                    Symbol.GLASS, 0,
                    Symbol.PAPER, 0
            )
    );
    // temporary data structure until we find a good algo for pattern matching
    private final @NotNull ImmutableMap<Color, Integer> colorsOfPattern;

    protected PositioningCard(@NotNull Builder builder) {
        super(builder);
        this.colorsOfPattern = Maps.immutableEnumMap(builder.colorsOfPattern);
    }

    @Override
    public ImmutableMap<Color, Integer> getColorRequirements() {
        return colorsOfPattern;
    }

    @Override
    public @NotNull ImmutableMap<Symbol, Integer> getSymbolRequirements() {
        return SYMBOL_REQUIREMENTS;
    }

    public static abstract class Builder extends ObjectiveCard.Builder {

        private final @NotNull EnumMap<Color, Integer> colorsOfPattern;

        protected Builder(int points) {
            super(points);
            colorsOfPattern = new EnumMap<>(Color.class);
        }

        public @NotNull Builder hasColor(@NotNull Color color, int number) {
            colorsOfPattern.put(color, number);
            return this;
        }
    }
}
