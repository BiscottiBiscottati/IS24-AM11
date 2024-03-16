package it.polimi.ingsw.am11.cards.playable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.cards.exceptions.IllegalBuildException;
import it.polimi.ingsw.am11.cards.utils.*;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Optional;

public class ResourceCard extends PlayableCard {
    private final static ImmutableMap<Color, Integer> PLACING_REQUIREMENTS = Maps.immutableEnumMap(
            EnumMapUtils.defaultInit(Color.class, 0)
    );
    private final ImmutableMap<Corner, CornerContainer> availableCornerOrItem;

    private ResourceCard(@NotNull Builder builder) {
        super(builder);
        this.availableCornerOrItem = Maps.immutableEnumMap(builder.availableCornerOrItem);
    }

    @Override
    @NotNull
    public PlayableCardType getType() {
        return PlayableCardType.RESOURCE;
    }

    @Override
    public boolean isAvailable(@NotNull Corner corner) {
        return availableCornerOrItem.getOrDefault(corner, Availability.NOT_USABLE).isAvailable();
    }

    @Override
    @NotNull
    public ImmutableMap<Color, Integer> getPlacingRequirements() {
        return PLACING_REQUIREMENTS;
    }

    @Override
    @NotNull
    public PointsRequirementsType getPointsRequirements() {
        return PointsRequirementsType.CLASSIC;
    }

    @Override
    @NotNull
    public CornerContainer checkItemCorner(@NotNull Corner corner) {
        return availableCornerOrItem.get(corner);
    }

    @Override
    @NotNull
    public Optional<Symbol> getSymbolToCollect() {
        return Optional.empty();
    }

    public static class Builder extends PlayableCard.Builder {
        private final EnumMap<Corner, CornerContainer> availableCornerOrItem;

        public Builder(int cardPoints, @NotNull Color cardPrimaryColor) throws IllegalBuildException {
            super(cardPoints, cardPrimaryColor);
            this.availableCornerOrItem = EnumMapUtils.defaultInit(Corner.class, Availability.NOT_USABLE);
        }

        @NotNull
        public ResourceCard build() {
            return new ResourceCard(this);
        }

        @NotNull
        public Builder hasItemIn(@NotNull Corner corner, CornerContainer cornerContainer) {
            availableCornerOrItem.put(corner, cornerContainer);
            return this;
        }

    }
}
