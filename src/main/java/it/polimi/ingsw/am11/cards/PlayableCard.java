package it.polimi.ingsw.am11.cards;

import java.util.EnumMap;
import java.util.Optional;

public abstract class PlayableCard {
    final Color color;
    final int points;

    protected PlayableCard(Color color, int points) {
        this.color = color;
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public Color getColor() {
        return color;
    }

    public abstract PlayableCardType getType();

    public abstract boolean isCornerAvail(Corner corner);

    public abstract EnumMap<Color, Integer> getPlacingRequirements();

    public abstract PointsRequirementsType getPointsRequirements();

    public abstract CornerContainer checkItemCorner(Corner corner);

    public abstract Optional<Symbol> getSymbolToCollect();

    public abstract static class Builder {
        protected int cardPoints;
        protected Color primaryColor;

        public Builder(int cardPoints, Color primaryColor) {
            this.cardPoints = cardPoints;
            this.primaryColor = primaryColor;
        }

        public abstract PlayableCard build();
    }


}
