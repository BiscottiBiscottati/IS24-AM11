package it.polimi.ingsw.am11.cards.objective;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.cards.utils.*;
import it.polimi.ingsw.am11.exceptions.IllegalBuildException;
import it.polimi.ingsw.am11.players.PlayerField;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumMap;

public class SymbolCollectCard extends CollectingCard {


    private static final ImmutableMap<Color, Integer> COLOR_TO_COLLECT = Maps.immutableEnumMap(
            EnumMapUtils.Init(Color.class, 0)
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

    @Override
    public int countPoints(
            PlayerField playerField) {
        return Arrays.stream(Symbol.values())
                     .filter(symbol -> symbolToCollect.get(symbol) != 0)
                     .map(symbol -> playerField.getNumberOf(symbol) / symbolToCollect.get(symbol))
                     .map(integer -> integer < 0 ? 0 : integer)
                     .mapToInt(Integer::intValue)
                     .min()
                     .orElse(0) * this.getPoints();
    }

    public static class Builder extends CollectingCard.Builder {

        private final EnumMap<Symbol, Integer> symbolToCollect;

        public Builder(int points) throws IllegalArgumentException {
            super(points);
            symbolToCollect = EnumMapUtils.Init(Symbol.class, 0);
        }

        public @NotNull Builder hasSymbol(@NotNull Symbol symbol, int quantity) {
            this.symbolToCollect.put(symbol, quantity);
            return this;
        }

        public @NotNull Builder hasSymbol(@NotNull Symbol symbol) {
            this.symbolToCollect.compute(symbol, (key, value) -> value + 1);
            return this;
        }

        @Override
        public @NotNull SymbolCollectCard build() throws IllegalBuildException {
            if (Validator.nonNegativeValues(symbolToCollect)) return new SymbolCollectCard(this);
            throw new IllegalBuildException("Symbols to collect cannot be less than 0!");
        }
    }
}
