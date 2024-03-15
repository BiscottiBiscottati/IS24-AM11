package it.polimi.ingsw.am11.cards.objective;

import it.polimi.ingsw.am11.cards.util.Color;
import it.polimi.ingsw.am11.cards.util.Symbol;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

public abstract class PositioningCard extends ObjectiveCard {
    private static final EnumMap<Symbol, Integer> SYMBOL_REQUIREMENTS = new EnumMap<>(Map.of(
            Symbol.FEATHER, 0,
            Symbol.GLASS, 0,
            Symbol.PAPER, 0
    ));
    // temporary data structure until we find a good algo for pattern matching
    private final EnumMap<Color, Integer> colorsOfPattern;

    protected PositioningCard(@NotNull Builder builder) {
        super(builder);
        this.colorsOfPattern = builder.colorsOfPattern;
    }

    @Override
    public EnumMap<Color, Integer> getColorRequirements() {
        return colorsOfPattern;
    }

    @Override
    public EnumMap<Symbol, Integer> getSymbolRequirements() {
        return SYMBOL_REQUIREMENTS;
    }

    public static abstract class Builder extends ObjectiveCard.Builder {

        private final EnumMap<Color, Integer> colorsOfPattern;

        protected Builder(int points) {
            super(points);
            colorsOfPattern = new EnumMap<>(Color.class);
        }

        public Builder hasColor(@NotNull Color color, int number) {
            colorsOfPattern.put(color, number);
            return this;
        }
    }
}
