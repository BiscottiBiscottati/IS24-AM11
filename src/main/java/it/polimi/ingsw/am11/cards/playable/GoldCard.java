package it.polimi.ingsw.am11.cards.playable;

import it.polimi.ingsw.am11.cards.*;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Optional;

public class GoldCard extends PlayableCard {

    private final EnumMap<Corner, Availability> availableCorners;
    private final EnumMap<Color, Integer> colorPlacingRequirements;
    private final PointsRequirementsType pointsRequirements;
    private final Symbol symbolToCollect;

    private GoldCard(@NotNull Builder builder) {
        super(builder);
        this.availableCorners = builder.availableCorners;
        this.colorPlacingRequirements = builder.colorPlacingRequirements;
        this.pointsRequirements = builder.pointsRequirements;
        this.symbolToCollect = builder.symbolToCollect;
    }

    @Override
    public PlayableCardType getType() {
        return PlayableCardType.GOLD;
    }

    @Override
    public boolean isCornerAvail(@NotNull Corner corner) {
        return availableCorners.getOrDefault(corner, Availability.NOT_USABLE).isAvailable();
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
    public CornerContainer checkItemCorner(@NotNull Corner corner) {
        return availableCorners.getOrDefault(corner, Availability.NOT_USABLE);
    }

    public static class Builder extends PlayableCard.Builder {

        private final EnumMap<Corner, Availability> availableCorners;
        private final EnumMap<Color, Integer> colorPlacingRequirements;
        private PointsRequirementsType pointsRequirements;
        private Symbol symbolToCollect;

        public Builder(int points, Color primaryColor) {
            super(points, primaryColor);
            this.availableCorners = new EnumMap<>(Corner.class);
            this.colorPlacingRequirements = new EnumMap<>(Color.class);
            this.symbolToCollect = null;

        }

        public Builder hasCorner(Corner corner, boolean available) {
            availableCorners.put(corner, available ? Availability.EMPTY : Availability.NOT_USABLE);
            return this;
        }

        public Builder hasRequirements(Color color, Integer number) {
            colorPlacingRequirements.put(color, number);
            return this;
        }

        public Builder hasPointsRequirements(PointsRequirementsType type) {
            this.pointsRequirements = type;
            if (!(type == PointsRequirementsType.SYMBOLS)) this.symbolToCollect = null;
            return this;
        }

        public Builder hasSymbol(Symbol symbol) {
            this.pointsRequirements = PointsRequirementsType.SYMBOLS;
            this.symbolToCollect = symbol;
            return this;
        }

        @Override
        public PlayableCard build() {
            return new GoldCard(this);
        }
    }
}
