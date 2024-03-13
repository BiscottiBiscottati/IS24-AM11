package it.polimi.ingsw.am11.Cards;

import java.util.EnumMap;
import java.util.Optional;

public abstract class PlayableCard {
    final Color cardColor;
    final int points;

    protected PlayableCard(Color cardColor, int points) {
        this.cardColor = cardColor;
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public Color getColor() {
        return cardColor;
    }

    public abstract PlayableCardType getType();

    public abstract boolean isCornerAvail(Corner corner);

    public abstract EnumMap<Color, Integer> getPlacingRequirements();

    public abstract PointsRequirementsType getPointsRequirements();

    public abstract Optional<Item> checkItemCorner(Corner corner);

    public abstract Optional<Symbol> getSymbolToCollect();

    public abstract static class Builder {
        protected int cardPoints;
        protected Color cardPrimaryColor;

        public Builder hasColor(Color color) {
            this.cardPrimaryColor = color;
            return this;
        }

        public Builder hasPoints(int points) {
            this.cardPoints = points;
            return this;
        }

        public abstract PlayableCard build();
    }


}
