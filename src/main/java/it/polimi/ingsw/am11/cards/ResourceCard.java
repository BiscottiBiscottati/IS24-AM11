package it.polimi.ingsw.am11.cards;

import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public class ResourceCard extends PlayableCard {
    private final static EnumMap<Color, Integer> defaultPlacingRequirements = new EnumMap<>(Map.of(
            Color.BLUE, 0,
            Color.RED, 0,
            Color.GREEN, 0,
            Color.PURPLE, 0
    ));
    private final EnumMap<Corner, CornerContainer> availableCornerOrItem;

    private ResourceCard(@NotNull Builder builder) {
        super(builder.primaryColor, builder.cardPoints);
        this.availableCornerOrItem = builder.availableCornerOrItem;
    }

    public static EnumMap<Color, Integer> getDefaultPlacingRequirements() {
        return defaultPlacingRequirements;
    }

    @Override
    public PlayableCardType getType() {
        return PlayableCardType.RESOURCE;
    }

    @Override
    public boolean isCornerAvail(Corner corner) {
        return availableCornerOrItem.getOrDefault(corner, Availability.NOT_USABLE).isAvailable();
    }

    @Override
    public EnumMap<Color, Integer> getPlacingRequirements() {
        return defaultPlacingRequirements;
    }

    @Override
    public PointsRequirementsType getPointsRequirements() {
        return PointsRequirementsType.CLASSIC;
    }

    @Override
    public CornerContainer checkItemCorner(Corner corner) {
        return availableCornerOrItem.getOrDefault(corner, Availability.NOT_USABLE);
    }

    @Override
    public Optional<Symbol> getSymbolToCollect() {
        return Optional.empty();
    }

    public static class Builder extends PlayableCard.Builder {
        private final EnumMap<Corner, CornerContainer> availableCornerOrItem;

        public Builder(int cardPoints, Color cardPrimaryColor) {
            super(cardPoints, cardPrimaryColor);
            this.availableCornerOrItem = new EnumMap<>(Corner.class);
        }

        public ResourceCard build() {
            return new ResourceCard(this);
        }

        public Builder hasItemIn(Corner corner, CornerContainer cornerContainer) {
            availableCornerOrItem.put(corner, cornerContainer);
            return this;
        }

    }
}
