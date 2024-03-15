package it.polimi.ingsw.am11.cards.playable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.cards.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Optional;

public class GoldCard extends PlayableCard {

    private final @NotNull ImmutableMap<Corner, Availability> availableCorners;
    private final @NotNull ImmutableMap<Color, Integer> colorPlacingRequirements;
    private final PointsRequirementsType pointsRequirements;
    private final Symbol symbolToCollect;

    private GoldCard(@NotNull Builder builder) {
        super(builder);
        this.availableCorners = Maps.immutableEnumMap(builder.availableCorners);
        this.colorPlacingRequirements = Maps.immutableEnumMap(builder.colorPlacingRequirements);
        this.pointsRequirements = builder.pointsRequirements;
        this.symbolToCollect = builder.symbolToCollect;
    }

    @Override
    public @NotNull PlayableCardType getType() {
        return PlayableCardType.GOLD;
    }

    @Override
    public boolean isAvailable(@NotNull Corner corner) {
        return availableCorners.getOrDefault(corner, Availability.NOT_USABLE).isAvailable();
    }

    @Override
    public ImmutableMap<Color, Integer> getPlacingRequirements() {
        return colorPlacingRequirements;
    }

    @Override
    public PointsRequirementsType getPointsRequirements() {
        return pointsRequirements;
    }

    public @NotNull Optional<Symbol> getSymbolToCollect() {
        return Optional.ofNullable(symbolToCollect);
    }

    @Override
    public @Nullable CornerContainer checkItemCorner(@NotNull Corner corner) {
        return availableCorners.getOrDefault(corner, Availability.NOT_USABLE);
    }

    public static class Builder extends PlayableCard.Builder {

        private final @NotNull EnumMap<Corner, Availability> availableCorners;
        private final @NotNull EnumMap<Color, Integer> colorPlacingRequirements;
        private PointsRequirementsType pointsRequirements;
        private @Nullable Symbol symbolToCollect;

        public Builder(int points, @NotNull Color primaryColor) {
            super(points, primaryColor);
            this.availableCorners = new EnumMap<>(Corner.class);
            this.colorPlacingRequirements = new EnumMap<>(Color.class);
            this.symbolToCollect = null;

        }

        public @NotNull Builder hasCorner(@NotNull Corner corner, boolean available) {
            availableCorners.put(corner, available ? Availability.EMPTY : Availability.NOT_USABLE);
            return this;
        }

        public @NotNull Builder hasCorner(@NotNull Corner corner) {
            availableCorners.put(corner, Availability.EMPTY);
            return this;
        }

        public @NotNull Builder hasRequirements(@NotNull Color color, Integer number) {
            colorPlacingRequirements.put(color, number);
            return this;
        }

        public @NotNull Builder hasPointsRequirements(@NotNull PointsRequirementsType type) {
            this.pointsRequirements = type;
            if (!(type == PointsRequirementsType.SYMBOLS)) this.symbolToCollect = null;
            return this;
        }

        public @NotNull Builder hasSymbolToCollect(@NotNull Symbol symbol) {
            this.pointsRequirements = PointsRequirementsType.SYMBOLS;
            this.symbolToCollect = symbol;
            return this;
        }

        @Override
        public @NotNull GoldCard build() {
            return new GoldCard(this);
        }
    }
}
