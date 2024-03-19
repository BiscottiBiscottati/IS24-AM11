package it.polimi.ingsw.am11.cards.playable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.cards.utils.*;
import it.polimi.ingsw.am11.exceptions.IllegalBuildException;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;


public abstract sealed class PlayableCard implements FieldCard permits GoldCard, ResourceCard {
    private static final ImmutableMap<Corner, Availability> retroCorners = Maps.immutableEnumMap(
            EnumMapUtils.Init(Corner.class, Availability.USABLE)
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

    @NotNull
    public abstract PlayableCardType getType();

    public abstract boolean isAvailable(@NotNull Corner corner);

    @NotNull
    public abstract ImmutableMap<Color, Integer> getPlacingRequirements();

    @NotNull
    public abstract PointsRequirementsType getPointsRequirements();

    @NotNull
    public abstract CornerContainer checkItemCorner(@NotNull Corner corner);

    @NotNull
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
