package it.polimi.ingsw.am11.cards.objective;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.cards.exceptions.IllegalBuildException;
import it.polimi.ingsw.am11.cards.utils.*;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public class ColorCollectCard extends CollectingCard {
    private static final ImmutableMap<Symbol, Integer> SYMBOL_TO_COLLECT = Maps.immutableEnumMap(
            EnumMapUtils.defaultInit(Symbol.class, 0)
    );

    private final @NotNull ImmutableMap<Color, Integer> colorToCollect;


    private ColorCollectCard(@NotNull Builder builder) {
        super(builder);
        this.colorToCollect = Maps.immutableEnumMap(builder.colorToCollect);

    }

    @Override
    public ImmutableMap<Symbol, Integer> getSymbolRequirements() {
        return SYMBOL_TO_COLLECT;
    }

    @Override
    public ImmutableMap<Color, Integer> getColorRequirements() {
        return this.colorToCollect;
    }

    @Override
    public @NotNull ObjectiveCardType getType() {
        return ObjectiveCardType.COLOR_COLLECT;
    }

    public static class Builder extends CollectingCard.Builder {

        private final @NotNull EnumMap<Color, Integer> colorToCollect;

        public Builder(int points) {
            super(points);
            this.colorToCollect = EnumMapUtils.defaultInit(Color.class, 0);
        }

        public @NotNull Builder hasColor(Color color, int quantity) {
            this.colorToCollect.put(color, quantity);
            return this;
        }

        public @NotNull Builder hasColor(Color color) {
            this.colorToCollect.compute(color, (key, value) -> value == null ? 0 : value + 1);
            return this;
        }

        @Override
        public @NotNull ColorCollectCard build() throws IllegalBuildException {
            if (Validator.nonNegativeValues(colorToCollect)) return new ColorCollectCard(this);
            else throw new IllegalBuildException("Colors to collect cannot be less than 0!");
        }
    }
}
