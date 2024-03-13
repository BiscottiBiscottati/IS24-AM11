package it.polimi.ingsw.am11.Cards;

import java.util.EnumMap;
import java.util.Optional;

public class GoldCard extends PlayableCard {

    private final EnumMap<Corner, Optional<Availability>> availableCorners;
    private final EnumMap<Color, Integer> colorPlacingRequirements;
    private final PointsRequirementsType pointsRequirements;
    private final Symbol symbolToCollect;

    private GoldCard(Builder builder) {
        super(builder.cardPrimaryColor, builder.cardPoints);
        this.availableCorners = availableCorners;
        this.colorPlacingRequirements = colorPlacingRequirements;
        this.pointsRequirements = pointsRequirements;
        this.symbolToCollect = symbolToCollect;
    }

    public static class Builder extends PlayableCard.Builder {

        private EnumMap<Corner, Optional<Availability>> availableCorners;
        private EnumMap<Color, Integer> colorPlacingRequirements;
        private PointsRequirementsType pointsRequirements;
        private Symbol symbolToCollect;

        public Builder() {
            this.availableCorners = new EnumMap<>(Corner.class);

        }

        public Builder hasCorner(Corner corner, boolean available) {
            if (available) availableCorners.put(corner, Optional.of(Availability.AVAILABLE));
            else availableCorners.put(corner, Optional.empty());
            return this;
        }

        public Builder hasRequirements(Color color, Integer number) {
            colorPlacingRequirements.put(color, number);
            return this;
        }

        @Override
        public PlayableCard build() {
            return new GoldCard(this);
        }
    }

    @Override
    public PlayableCardType getType() {
        return PlayableCardType.GOLD;
    }

    @Override
    public boolean isCornerAvail(Corner corner) {
        return availableCorners.get(corner).isPresent();
    }

    @Override
    public EnumMap<Color, Integer> getPlacingRequirements() {
        return colorPlacingRequirements;
    }

    @Override
    public PointsRequirementsType getPointsRequirements() {
        return pointsRequirements;
    }

    public Optional<Symbol> getSymbolToCollect() {
        return Optional.ofNullable(symbolToCollect);
    }

    @Override
    public Optional<Item> checkItemCorner(Corner corner) {
        return availableCorners.get(corner).map(signal -> signal);
    }
}
