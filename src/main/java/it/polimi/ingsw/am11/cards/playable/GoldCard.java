package it.polimi.ingsw.am11.cards.playable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.cards.utils.*;
import it.polimi.ingsw.am11.exceptions.IllegalBuildException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Objects;
import java.util.Optional;

public final class GoldCard extends PlayableCard {

    private final ImmutableMap<Corner, Availability> availableCorners;
    private final ImmutableMap<Color, Integer> colorPlacingRequirements;
    private final PointsRequirementsType pointsRequirements;
    private final Symbol symbolToCollect;

    /**
     * @param builder The builder for creation of a new instance
     */
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
        return Objects.requireNonNull(availableCorners.get(corner)).isAvailable();
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
        return Objects.requireNonNull(availableCorners.get(corner));
    }

    public static class Builder extends PlayableCard.Builder {

        private final EnumMap<Corner, Availability> availableCorners;
        private final EnumMap<Color, Integer> colorPlacingRequirements;
        private PointsRequirementsType pointsRequirements;
        private @Nullable Symbol symbolToCollect;


        /**
         * @param points       The points value of the card
         * @param primaryColor The color of the card
         * @throws IllegalBuildException if points are negative
         */
        public Builder(int points, @NotNull Color primaryColor) throws IllegalBuildException {
            super(points, primaryColor);
            this.availableCorners = EnumMapUtils.Init(Corner.class, Availability.NOT_USABLE);
            this.colorPlacingRequirements = EnumMapUtils.Init(Color.class, 0);
            this.symbolToCollect = null;

        }

        /**
         * @param corner    The corner to set
         * @param available if the corner should be available to cover
         * @return The modified builder
         */
        public Builder hasCorner(@NotNull Corner corner, boolean available) {
            availableCorners.put(corner, available ? Availability.USABLE : Availability.NOT_USABLE);
            return this;
        }

        /**
         * @param corner The corner to set as available to cover
         * @return The modified builder
         */
        public Builder hasCorner(@NotNull Corner corner) {
            availableCorners.put(corner, Availability.USABLE);
            return this;
        }

        /**
         * @param color  The color requirement to place
         * @param number The number of color to have in field
         * @return The modified builder
         */
        public Builder hasRequirements(@NotNull Color color, Integer number) {
            colorPlacingRequirements.put(color, number);
            return this;
        }

        /**
         * @param type Set how to get points if placed
         * @return The modified builder
         * @see PointsRequirementsType
         */
        public Builder hasPointsRequirements(@NotNull PointsRequirementsType type) {
            this.pointsRequirements = type;
            if (!(type == PointsRequirementsType.SYMBOLS)) this.symbolToCollect = null;
            return this;
        }

        /**
         * @param symbol if not null sets a symbol to collect for scoring points otherwise does nothing
         * @return The builder with its values set or the same previous builder
         */
        public Builder hasSymbolToCollect(@Nullable Symbol symbol) {
            if (symbol == null) return this;
            this.pointsRequirements = PointsRequirementsType.SYMBOLS;
            this.symbolToCollect = symbol;
            return this;
        }

        /**
         * @return The new GoldCard created with builder's values set previously
         * @throws IllegalBuildException if placement requirements to place have a negative value
         */
        @Override
        @NotNull
        public GoldCard build() throws IllegalBuildException {
            if (Validator.nonNegativeValues(colorPlacingRequirements)) return new GoldCard(this);
            throw new IllegalBuildException("Placing requirements cannot be less than 0!");
        }
    }
}
