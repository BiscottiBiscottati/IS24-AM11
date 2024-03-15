package it.polimi.ingsw.am11.cards.playable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.cards.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Optional;

public class ResourceCard extends PlayableCard {
    private final static ImmutableMap<Color, Integer> PLACING_REQUIREMENTS = Maps.immutableEnumMap(
            ImmutableMap.of(
                    Color.RED, 0,
                    Color.BLUE, 0,
                    Color.GREEN, 0,
                    Color.PURPLE, 0
            )
    );
    private final @NotNull ImmutableMap<Corner, CornerContainer> availableCornerOrItem;

    private ResourceCard(@NotNull Builder builder) {
        super(builder);
        this.availableCornerOrItem = Maps.immutableEnumMap(builder.availableCornerOrItem);
    }

    @Override
    public @NotNull PlayableCardType getType() {
        return PlayableCardType.RESOURCE;
    }

    @Override
    public boolean isAvailable(@NotNull Corner corner) {
        return availableCornerOrItem.getOrDefault(corner, Availability.NOT_USABLE).isAvailable();
    }

    @Override
    public ImmutableMap<Color, Integer> getPlacingRequirements() {
        return PLACING_REQUIREMENTS;
    }

    @Override
    public @NotNull PointsRequirementsType getPointsRequirements() {
        return PointsRequirementsType.CLASSIC;
    }

    @Override
    public @Nullable CornerContainer checkItemCorner(@NotNull Corner corner) {
        return availableCornerOrItem.getOrDefault(corner, Availability.NOT_USABLE);
    }

    @Override
    public @NotNull Optional<Symbol> getSymbolToCollect() {
        return Optional.empty();
    }

    public static class Builder extends PlayableCard.Builder {
        private final @NotNull EnumMap<Corner, CornerContainer> availableCornerOrItem;

        public Builder(int cardPoints, @NotNull Color cardPrimaryColor) {
            super(cardPoints, cardPrimaryColor);
            this.availableCornerOrItem = new EnumMap<>(Corner.class);
        }

        public @NotNull ResourceCard build() {
            return new ResourceCard(this);
        }

        public @NotNull Builder hasItemIn(@NotNull Corner corner, CornerContainer cornerContainer) {
            availableCornerOrItem.put(corner, cornerContainer);
            return this;
        }

    }
}
