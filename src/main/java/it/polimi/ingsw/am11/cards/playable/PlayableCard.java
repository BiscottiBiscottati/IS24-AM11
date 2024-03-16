package it.polimi.ingsw.am11.cards.playable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.cards.exceptions.IllegalBuildException;
import it.polimi.ingsw.am11.cards.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class PlayableCard {
    private static final ImmutableMap<Corner, Availability> retroCorners = Maps.immutableEnumMap(
            EnumMapUtils.defaultInit(Corner.class, Availability.USABLE)
    );
    private final Color color;
    private final int points;

    protected PlayableCard(@NotNull Builder builder) {
        this.color = builder.primaryColor;
        this.points = builder.cardPoints;
    }

    public static boolean isRetroAvailable(@NotNull Corner corner) {
        return retroCorners.get(corner).isAvailable();
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

        protected Builder(int cardPoints, @NotNull Color primaryColor) throws IllegalBuildException {
            if (cardPoints < 0) throw new IllegalBuildException("Points cannot be less than 0!");
            this.cardPoints = cardPoints;
            this.primaryColor = primaryColor;
        }

        public abstract PlayableCard build() throws IllegalBuildException;
    }


}
