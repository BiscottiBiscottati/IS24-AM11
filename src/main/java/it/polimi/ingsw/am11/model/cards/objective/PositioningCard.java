package it.polimi.ingsw.am11.model.cards.objective;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.model.cards.objective.positioning.LCard;
import it.polimi.ingsw.am11.model.cards.objective.positioning.TripletCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public abstract sealed class PositioningCard extends ObjectiveCard
        permits LCard, TripletCard {
    private static final ImmutableMap<Symbol, Integer> SYMBOL_REQUIREMENTS = Maps.immutableEnumMap(
            ImmutableMap.of(
                    Symbol.FEATHER, 0,
                    Symbol.GLASS, 0,
                    Symbol.PAPER, 0
            )
    );

    private final @NotNull ImmutableMap<Color, Integer> colorRequirements;

    protected PositioningCard(@NotNull Builder<?> builder,
                              @NotNull EnumMap<Color, Integer> colorRequirements) {
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

    public abstract List<List<Color>> getPattern();

    public static abstract class Builder<T extends PositioningCard>
            extends ObjectiveCard.Builder<PositioningCard> {

        protected Builder(int id, int points) {
            super(id, points);
        }

        @Override
        public abstract T build() throws IllegalCardBuildException;
    }
}
