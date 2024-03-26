package it.polimi.ingsw.am11.cards.objective;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.ObjectiveCardType;
import it.polimi.ingsw.am11.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.cards.utils.helpers.EnumMapUtils;
import it.polimi.ingsw.am11.cards.utils.helpers.Validator;
import it.polimi.ingsw.am11.exceptions.IllegalBuildException;
import it.polimi.ingsw.am11.players.PlayerField;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Objects;

public class ColorCollectCard extends CollectingCard {
    private static final ImmutableMap<Symbol, Integer> SYMBOL_TO_COLLECT = Maps.immutableEnumMap(
            EnumMapUtils.Init(Symbol.class, 0)
    );

    private final @NotNull ImmutableMap<Color, Integer> colorToCollect;


    private ColorCollectCard(@NotNull Builder builder) {
        super(builder);
        this.colorToCollect = Maps.immutableEnumMap(builder.colorToCollect);

    }

    @Override
    public @NotNull ImmutableMap<Symbol, Integer> getSymbolRequirements() {
        return SYMBOL_TO_COLLECT;
    }

    @Override
    public @NotNull ImmutableMap<Color, Integer> getColorRequirements() {
        return this.colorToCollect;
    }

    @Override
    public @NotNull ObjectiveCardType getType() {
        return ObjectiveCardType.COLOR_COLLECT;
    }

    @Override
    public int countPoints(
            PlayerField playerField) {
        return Arrays.stream(Color.values())
                     .filter(color -> Objects.requireNonNull(colorToCollect.get(color)) != 0)
                     .map(color -> playerField.getNumberOf(color) /
                             Objects.requireNonNull(colorToCollect.get(color)))
                     .map(integer -> integer < 0 ? 0 : integer)
                     .mapToInt(Integer::intValue)
                     .min()
                     .orElse(0) * this.getPoints();

    }

    public static class Builder extends CollectingCard.Builder<ColorCollectCard> {

        private final @NotNull EnumMap<Color, Integer> colorToCollect;

        public Builder(int id, int points) {
            super(id, points);
            this.colorToCollect = EnumMapUtils.Init(Color.class, 0);
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
