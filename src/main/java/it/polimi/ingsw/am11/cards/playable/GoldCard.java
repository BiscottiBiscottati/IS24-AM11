package it.polimi.ingsw.am11.cards.playable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.cards.utils.enums.*;
import it.polimi.ingsw.am11.cards.utils.helpers.EnumMapUtils;
import it.polimi.ingsw.am11.cards.utils.helpers.Validator;
import it.polimi.ingsw.am11.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.players.PlayerField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Objects;
import java.util.Optional;

/**
 * This class represents a gold card.
 * <p>
 * Each attribute is immutable.
 * <p>
 * The class can only be instantiated through the static inner builder
 * {@link GoldCard.Builder}.
 */
public final class GoldCard extends PlayableCard {

    private final ImmutableMap<Corner, CornerContainer> availableCornersOrSymbol;
    private final ImmutableMap<Color, Integer> colorPlacingRequirements;
    private final PointsRequirementsType pointsRequirements;
    private final Symbol symbolToCollect;

    /**
     * Constructor called from build method of <code>GoldCard.Builder</code>.
     *
     * @param builder the builder for creation of a new instance
     */
    private GoldCard(@NotNull Builder builder) {
        super(builder);
        this.availableCornersOrSymbol = Maps.immutableEnumMap(builder.availableCorners);
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
        return Objects.requireNonNull(availableCornersOrSymbol.get(corner)).isAvailable();
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
        return Objects.requireNonNull(availableCornersOrSymbol.get(corner));
    }

    @Override
    public int countPoints(@NotNull PlayerField playerField) {
        switch (this.pointsRequirements) {
            case CLASSIC -> {
                return super.countPoints(playerField);
            }
            case SYMBOLS -> {
                return playerField.getNumberOf(this.symbolToCollect) * this.getPoints();
            }
            case COVERING_CORNERS -> {
                return 0; //TODO : not done yet
            }
            default -> throw new IllegalStateException("Unexpected value: " + this.pointsRequirements);
        }
    }

    /**
     * Builder class for creating instances of {@link GoldCard}.
     * This builder provides methods to set the required attributes
     * for the target object.
     */
    public static class Builder extends PlayableCard.Builder<GoldCard> {

        private final EnumMap<Corner, CornerContainer> availableCorners;
        private final EnumMap<Color, Integer> colorPlacingRequirements;
        private PointsRequirementsType pointsRequirements;
        private @Nullable Symbol symbolToCollect;


        /**
         * Constructor for <code>Builder</code> that's needed for creation of a <code>GoldCard</code>.
         *
         * @param points       the point value of the card
         * @param primaryColor the color of the card
         * @throws IllegalCardBuildException if points are negative
         */
        public Builder(int id, int points, @NotNull Color primaryColor) throws IllegalCardBuildException {
            super(id, points, primaryColor);
            this.availableCorners = EnumMapUtils.Init(Corner.class, Availability.NOT_USABLE);
            this.colorPlacingRequirements = EnumMapUtils.Init(Color.class, 0);
            this.symbolToCollect = null;

        }

        /**
         * Sets if a corner can be covered by another card.
         *
         * @param corner    the corner to set
         * @param available if the corner should be available to cover
         * @return the modified builder
         */
        public Builder hasCorner(@NotNull Corner corner, boolean available) {
            availableCorners.put(corner, available ? Availability.USABLE : Availability.NOT_USABLE);
            return this;
        }

        /**
         * Sets the corner as available to be covered.
         *
         * @param corner The corner to set as available to cover
         * @return The modified builder
         */
        public Builder hasCorner(@NotNull Corner corner) {
            availableCorners.put(corner, Availability.USABLE);
            return this;
        }

        public Builder hasIn(Corner corner, @NotNull CornerContainer item) throws IllegalCardBuildException {
            switch (item) {
                case Availability ignored -> availableCorners.put(corner, item);
                case Symbol ignored -> availableCorners.put(corner, item);
                default -> throw new IllegalCardBuildException("Invalid corner container for GoldCard!");
            }
            return this;
        }

        /**
         * Specifies the color requirement for placing this card on the field.
         *
         * @param color  The color required for placement
         * @param number The number of the specified color needed on the field
         * @return The modified builder
         */
        public Builder hasRequirements(@NotNull Color color, Integer number) {
            colorPlacingRequirements.put(color, number);
            return this;
        }

        /**
         * Specifies the <code>PointsRequirementsType</code> of the card.
         *
         * @param type The type of requirements to set
         * @return The modified builder
         * @see PointsRequirementsType
         */
        public Builder hasPointRequirements(@NotNull PointsRequirementsType type) {
            this.pointsRequirements = type;
            if (!(type == PointsRequirementsType.SYMBOLS)) this.symbolToCollect = null;
            return this;
        }

        /**
         * Specifies a symbol to collect and sets the card as a <code>PointsRequirementsType.SYMBOL</code>.
         * <p>
         * If the provided argument is null, the method does nothing and returns the Builder.
         *
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
         * Constructs a new instance of <code>GoldCard</code> using the
         * parameters set by the builder's methods.
         *
         * @return A fully constructed instance of <code>GoldCard</code>.
         * @throws IllegalCardBuildException if placement requirements to place have a negative value
         */
        @Override
        @NotNull
        public GoldCard build() throws IllegalCardBuildException {
            if (this.pointsRequirements == null) throw new IllegalCardBuildException("No points requirements!");
            if (!Validator.nonNegativeValues(colorPlacingRequirements))
                throw new IllegalCardBuildException("Placing requirements cannot be less than 0!");
            return new GoldCard(this);
        }
    }
}
