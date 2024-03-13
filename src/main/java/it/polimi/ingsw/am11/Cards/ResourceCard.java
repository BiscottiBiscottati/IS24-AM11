package it.polimi.ingsw.am11.Cards;

import java.util.EnumMap;
import java.util.Optional;

public class ResourceCard extends PlayableCard {
    final EnumMap<Corner, Optional<Item>> availableCornerOrItem;

    protected ResourceCard(Color cardColor, int points, EnumMap<Corner, Optional<Item>> availableCornerOrItem) {
        super(cardColor, points);
        this.availableCornerOrItem = availableCornerOrItem;
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
        return null;
    }

    @Override
    public PointsRequirementsType getPointsRequirements() {
        return null;
    }

    @Override
    public Optional<Item> checkItemCorner() {
        return Optional.empty();
    }
}
