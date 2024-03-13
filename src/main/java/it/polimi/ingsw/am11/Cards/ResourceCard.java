package it.polimi.ingsw.am11.Cards;

import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public class ResourceCard extends PlayableCard {
    private final EnumMap<Corner, Optional<Item>> availableCornerOrItem;
    private final static EnumMap<Color, Integer> defaultPlacingRequirements = new EnumMap<>(Map.of(
            Color.BLUE, 0,
            Color.RED, 0,
            Color.GREEN, 0,
            Color.PURPLE, 0
    ));

    private ResourceCard(@NotNull Builder builder) {
        super(builder.cardPrimaryColor, builder.cardPoints);
        this.availableCornerOrItem = builder.availableCornerOrItem;
    }

    public static class Builder extends PlayableCard.Builder {

        private EnumMap<Corner, Optional<Item>> availableCornerOrItem;

        public ResourceCard build() {
            return new ResourceCard(this);
        }

        public Builder hasItemIn(Corner corner, Item item) {
            availableCornerOrItem.put(corner, Optional.of(item));
            return this;
        }

        public Builder hasEmpty(Corner corner) {
            availableCornerOrItem.put(corner, Optional.empty());
            return this;
        }
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
        return availableCornerOrItem.get(corner).isPresent();
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
    public Optional<Item> checkItemCorner(Corner corner) {
        return availableCornerOrItem.get(corner);
    }

    @Override
    public Optional<Symbol> getSymbolToCollect() {
        return Optional.empty();
    }
}
