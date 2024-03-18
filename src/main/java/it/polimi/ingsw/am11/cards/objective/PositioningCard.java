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

    private final ImmutableMap<Color, Integer> colorRequirements;

    protected PositioningCard(@NotNull Builder builder, EnumMap<Color, Integer> colorRequirements) {
        super(builder);
        this.colorRequirements = Maps.immutableEnumMap(colorRequirements);
    }

    @Override
    public @NotNull ImmutableMap<Symbol, Integer> getSymbolRequirements() {
        return SYMBOL_REQUIREMENTS;
    }

    public static abstract class Builder extends ObjectiveCard.Builder {

        protected Builder(int points) {
            super(points);
        }
    }
}
