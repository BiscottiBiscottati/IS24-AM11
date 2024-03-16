package it.polimi.ingsw.am11.cards.playable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.cards.exceptions.IllegalBuildException;
import it.polimi.ingsw.am11.cards.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Optional;

public class GoldCard extends PlayableCard {

    private final ImmutableMap<Corner, Availability> availableCorners;
    private final ImmutableMap<Color, Integer> colorPlacingRequirements;
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
    @NotNull
    public PlayableCardType getType() {
        return PlayableCardType.GOLD;
    }

    @Override
    public boolean isAvailable(@NotNull Corner corner) {
        return availableCorners.get(corner).isAvailable();
    }

    @Override
    @NotNull
    public ImmutableMap<Color, Integer> getPlacingRequirements() {
        return colorPlacingRequirements;
    }

    @Override
    @NotNull
    public PointsRequirementsType getPointsRequirements() {
        return pointsRequirements;
    }

    @Override
    @NotNull
    public Optional<Symbol> getSymbolToCollect() {
        return Optional.ofNullable(symbolToCollect);
    }

    @Override
    @NotNull
    public CornerContainer checkItemCorner(@NotNull Corner corner) {
        return availableCorners.get(corner);
    }

    public static class Builder extends PlayableCard.Builder {

        private final EnumMap<Corner, Availability> availableCorners;
        private final EnumMap<Color, Integer> colorPlacingRequirements;
        private PointsRequirementsType pointsRequirements;
        private @Nullable Symbol symbolToCollect;


        public Builder(int points, @NotNull Color primaryColor) throws IllegalBuildException {
            super(points, primaryColor);
            this.availableCorners = EnumMapUtils.defaultInit(Corner.class, Availability.NOT_USABLE);
            this.colorPlacingRequirements = EnumMapUtils.defaultInit(Color.class, 0);
            this.symbolToCollect = null;

        }

        public Builder hasCorner(@NotNull Corner corner, boolean available) {
            availableCorners.put(corner, available ? Availability.USABLE : Availability.NOT_USABLE);
            return this;
        }

        public Builder hasCorner(@NotNull Corner corner) {
            availableCorners.put(corner, Availability.USABLE);
            return this;
        }

        public Builder hasRequirements(@NotNull Color color, Integer number) {
            colorPlacingRequirements.put(color, number);
            return this;
        }

        public Builder hasPointsRequirements(@NotNull PointsRequirementsType type) {
            this.pointsRequirements = type;
            if (!(type == PointsRequirementsType.SYMBOLS)) this.symbolToCollect = null;
            return this;
        }

        public Builder hasSymbolToCollect(@NotNull Symbol symbol) {
            this.pointsRequirements = PointsRequirementsType.SYMBOLS;
            this.symbolToCollect = symbol;
            return this;
        }

        @Override
        @NotNull
        public GoldCard build() throws IllegalBuildException {
            if (Validator.nonNegativeValues(colorPlacingRequirements)) return new GoldCard(this);
            throw new IllegalBuildException("Placing requirements cannot be less than 0!");
        }
    }
}
