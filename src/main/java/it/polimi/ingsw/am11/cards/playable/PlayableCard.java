package it.polimi.ingsw.am11.cards.playable;

import com.google.common.collect.ImmutableMap;
import it.polimi.ingsw.am11.cards.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Optional;

public abstract class PlayableCard {
    final Color color;
    final int points;

    protected PlayableCard(@NotNull Builder builder) {
        this.color = builder.primaryColor;
        this.points = builder.cardPoints;
    }

    public int getPoints() {
        return points;
    }

    public Color getColor() {
        return color;
    }

    public abstract PlayableCardType getType();

    public abstract boolean isAvailable(@NotNull Corner corner);

    public abstract ImmutableMap<Color, Integer> getPlacingRequirements();

    public abstract PointsRequirementsType getPointsRequirements();

    public abstract @Nullable CornerContainer checkItemCorner(@NotNull Corner corner);

    public abstract Optional<Symbol> getSymbolToCollect();

    public abstract static class Builder {
        private final int cardPoints;
        private final @NotNull Color primaryColor;

        protected Builder(int cardPoints, @NotNull Color primaryColor) {
            this.cardPoints = cardPoints;
            this.primaryColor = primaryColor;
        }

        protected static <K extends Enum<K>, V> @NotNull EnumMap<K, V> InitEnumMap(Class<K> keyType, V value) {
            EnumMap<K, V> temp = new EnumMap<>(keyType);
            for (K key : keyType.getEnumConstants()) {
                temp.put(key, value);
            }
            return temp;
        }

        public abstract PlayableCard build();
    }


}
