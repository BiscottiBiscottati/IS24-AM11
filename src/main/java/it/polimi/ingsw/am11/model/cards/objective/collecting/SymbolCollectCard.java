package it.polimi.ingsw.am11.model.cards.objective.collecting;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.model.cards.objective.CollectingCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.ObjectiveCardType;
import it.polimi.ingsw.am11.model.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.model.cards.utils.helpers.EnumMapUtils;
import it.polimi.ingsw.am11.model.cards.utils.helpers.Validator;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.model.players.field.PlayerField;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class SymbolCollectCard extends CollectingCard {


    private static final ImmutableMap<Color, Integer> COLOR_TO_COLLECT = Maps.immutableEnumMap(
            EnumMapUtils.init(Color.class, 0)
    );
    private final @NotNull ImmutableMap<Symbol, Integer> symbolToCollect;

    private SymbolCollectCard(@NotNull Builder builder) {
        super(builder);
        this.symbolToCollect = Maps.immutableEnumMap(builder.symbolToCollect);
    }

    @Override
    public @NotNull Map<Color, Integer> getColorRequirements() {
        return COLOR_TO_COLLECT;
    }

    @Override
    public @NotNull Map<Symbol, Integer> getSymbolRequirements() {
        return symbolToCollect;
    }

    @Override
    @NotNull
    public ObjectiveCardType getType() {
        return ObjectiveCardType.SYMBOL_COLLECT;
    }

    @Override
    public int countPoints(
            @NotNull PlayerField playerField) {
        return Arrays.stream(Symbol.values())
                     .filter(symbol -> Objects.requireNonNull(symbolToCollect.get(symbol)) != 0)
                     .map(symbol -> playerField.getNumberOf(symbol) /
                                    Objects.requireNonNull(symbolToCollect.get(symbol)))
                     .map(integer -> integer < 0 ? 0 : integer)
                     .mapToInt(Integer::intValue)
                     .min()
                     .orElse(0) * this.getPoints();
    }

    public static class Builder extends CollectingCard.Builder<SymbolCollectCard> {

        private final @NotNull EnumMap<Symbol, Integer> symbolToCollect;

        public Builder(int id, int points) throws IllegalArgumentException {
            super(id, points);
            symbolToCollect = EnumMapUtils.init(Symbol.class, 0);
        }

        public @NotNull Builder hasSymbol(@NotNull Symbol symbol, int quantity) {
            this.symbolToCollect.put(symbol, quantity);
            return this;
        }

        public @NotNull Builder hasSymbol(@NotNull Symbol symbol) {
            this.symbolToCollect.compute(symbol, (key, value) -> value == null ? 0 : value + 1);
            return this;
        }

        @Override
        public @NotNull SymbolCollectCard build() throws IllegalCardBuildException {
            if (Validator.nonNegativeValues(symbolToCollect)) return new SymbolCollectCard(this);
            throw new IllegalCardBuildException("Symbols to collect cannot be less than 0!");
        }
    }
}
