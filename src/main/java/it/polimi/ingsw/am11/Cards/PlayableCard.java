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

    public abstract Optional<Item> checkItemCorner();

    public static abstract class Builder {
        int cardPoints;
        Color cardPrimaryColor;

        public void hasColor(Color color) {
            this.cardPrimaryColor = color;
        }

        public void hasPoints(int points) {
            this.cardPoints = points;
        }

        public abstract PlayableCard build();
    }


}
