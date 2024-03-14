package it.polimi.ingsw.am11.cards;

import org.jetbrains.annotations.NotNull;

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

    public abstract boolean isCornerAvail(@NotNull Corner corner);

    public abstract EnumMap<Color, Integer> getPlacingRequirements();

    public abstract PointsRequirementsType getPointsRequirements();

    public abstract CornerContainer checkItemCorner(@NotNull Corner corner);

    public abstract Optional<Symbol> getSymbolToCollect();

    public abstract static class Builder {
        private final int cardPoints;
        private final Color primaryColor;

        public Builder(int cardPoints, @NotNull Color primaryColor) {
            this.cardPoints = cardPoints;
            this.primaryColor = primaryColor;
        }

        public abstract PlayableCard build();
    }


}
