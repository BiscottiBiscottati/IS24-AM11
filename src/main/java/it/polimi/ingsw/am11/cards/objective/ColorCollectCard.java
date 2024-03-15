package it.polimi.ingsw.am11.cards.objective;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.cards.util.Color;
import it.polimi.ingsw.am11.cards.util.ObjectiveCardType;
import it.polimi.ingsw.am11.cards.util.Symbol;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public class ColorCollectCard extends CollectingCard {
    private static final ImmutableMap<Symbol, Integer> SYMBOL_TO_COLLECT = Maps.immutableEnumMap(
            ImmutableMap.of(
                    Symbol.FEATHER, 0,
                    Symbol.PAPER, 0,
                    Symbol.GLASS, 0
            )
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
            this.colorToCollect = new EnumMap<>(Color.class);
        }

        public @NotNull Builder hasColor(Color color, int quantity) {
            this.colorToCollect.put(color, quantity);
            return this;
        }

        public @NotNull Builder hasColor(Color color) {
            this.colorToCollect.put(color, 1);
            return this;
        }

        @Override
        public @NotNull ObjectiveCard build() {
            return new ColorCollectCard(this);
        }
    }
}
