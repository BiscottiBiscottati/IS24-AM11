package it.polimi.ingsw.am11.cards.objective;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.cards.exceptions.IllegalBuildException;
import it.polimi.ingsw.am11.cards.utils.*;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public class SymbolCollectCard extends CollectingCard {


    private static final ImmutableMap<Color, Integer> COLOR_TO_COLLECT = Maps.immutableEnumMap(
            EnumMapUtils.defaultInit(Color.class, 0)
    );
    private final ImmutableMap<Symbol, Integer> symbolToCollect;

    private SymbolCollectCard(@NotNull Builder builder) {
        super(builder);
        this.symbolToCollect = Maps.immutableEnumMap(builder.symbolToCollect);
    }

    @Override
    @NotNull
    public ImmutableMap<Symbol, Integer> getSymbolRequirements() {
        return symbolToCollect;
    }

    @Override
    @NotNull
    public ImmutableMap<Color, Integer> getColorRequirements() {
        return COLOR_TO_COLLECT;
    }

    @Override
    @NotNull
    public ObjectiveCardType getType() {
        return ObjectiveCardType.OBJECT_COLLECT;
    }

    public static class Builder extends CollectingCard.Builder {

        private final EnumMap<Symbol, Integer> symbolToCollect;

        public Builder(int points) {
            super(points);
            symbolToCollect = new EnumMap<>(Symbol.class);
        }

        public @NotNull Builder hasSymbol(@NotNull Symbol symbol, int quantity) {
            this.symbolToCollect.put(symbol, quantity);
            return this;
        }

        public @NotNull Builder hasSymbol(@NotNull Symbol symbol) {
            this.symbolToCollect.put(symbol, 1);
            return this;
        }

        @Override
        public @NotNull SymbolCollectCard build() throws IllegalBuildException {
            if (Validator.nonNegativeValues(symbolToCollect)) return new SymbolCollectCard(this);
            throw new IllegalBuildException("Symbols to collect cannot be less than 0!");
        }
    }
}
