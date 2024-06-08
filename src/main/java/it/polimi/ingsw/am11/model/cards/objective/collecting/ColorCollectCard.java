package it.polimi.ingsw.am11.model.cards.objective.collecting;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.model.cards.objective.CollectingCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.ObjectiveCardType;
import it.polimi.ingsw.am11.model.cards.utils.helpers.EnumMapUtils;
import it.polimi.ingsw.am11.model.cards.utils.helpers.Validator;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.model.players.field.PlayerField;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * This is the <code>ColorCollectCard</code> class that extends the <code>CollectingCard</code>
 * class. It represents an objective card for collecting a specific color. The class is final,
 * meaning it cannot be subclassed.
 * <p>
 * The class contains methods to get color, get the type of the card, count points based on the
 * player's field, and a nested Builder class for creating instances of ColorCollectCard.
 */
public final class ColorCollectCard extends CollectingCard {

    private final @NotNull ImmutableMap<Color, Integer> colorToCollect;


    private ColorCollectCard(@NotNull Builder builder) {
        super(builder);
        this.colorToCollect = Maps.immutableEnumMap(builder.colorToCollect);

    }

    @Override
    public @NotNull Map<Color, Integer> getColorRequirements() {
        return this.colorToCollect;
    }

    @Override
    public @NotNull ObjectiveCardType getType() {
        return ObjectiveCardType.COLOR_COLLECT;
    }

    @Override
    public int countPoints(
            @NotNull PlayerField playerField) {
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
            this.colorToCollect = EnumMapUtils.init(Color.class, 0);
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
        public @NotNull ColorCollectCard build() throws IllegalCardBuildException {
            if (Validator.nonNegativeValues(colorToCollect)) return new ColorCollectCard(this);
            else throw new IllegalCardBuildException("Colors to collect cannot be less than 0!");
        }
    }
}
