package it.polimi.ingsw.am11.cards.objective;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.exceptions.IllegalCardBuildException;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

public abstract class PositioningCard extends ObjectiveCard {
    private static final ImmutableMap<Symbol, Integer> SYMBOL_REQUIREMENTS = Maps.immutableEnumMap(
            ImmutableMap.of(
                    Symbol.FEATHER, 0,
                    Symbol.GLASS, 0,
                    Symbol.PAPER, 0
            )
    );

    private final ImmutableMap<Color, Integer> colorRequirements;

    protected PositioningCard(@NotNull Builder<?> builder, EnumMap<Color, Integer> colorRequirements) {
        super(builder);
        this.colorRequirements = Maps.immutableEnumMap(colorRequirements);
    }

    @Override
    public @NotNull Map<Color, Integer> getColorRequirements() {
        return colorRequirements;
    }

    @Override
    public @NotNull Map<Symbol, Integer> getSymbolRequirements() {
        return SYMBOL_REQUIREMENTS;
    }

    public static abstract class Builder<T extends PositioningCard>
            extends ObjectiveCard.Builder<PositioningCard> {

        protected Builder(int id, int points) {
            super(id, points);
        }

        @Override
        public abstract T build() throws IllegalCardBuildException;
    }
}
