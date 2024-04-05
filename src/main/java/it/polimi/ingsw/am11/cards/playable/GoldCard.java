package it.polimi.ingsw.am11.cards.playable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.cards.utils.enums.*;
import it.polimi.ingsw.am11.cards.utils.helpers.EnumMapUtils;
import it.polimi.ingsw.am11.cards.utils.helpers.Validator;
import it.polimi.ingsw.am11.exceptions.IllegalBuildException;
import org.jetbrains.annotations.Contract;
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

    private final ImmutableMap<Corner, Availability> availableCorners;
    private final ImmutableMap<Color, Integer> colorPlacingRequirements;
    private final PointsRequirementsType pointsRequirements;
    private final Symbol symbolToCollect;

    /**
     * Private constructor for creating a new instance of the <code>GoldCard</code> class.
     * <p>
     * This constructor is called from the <code>build</code> method of
     * the <code>GoldCard.Builder</code> class.
     * It initializes the <code>availableCorners</code>, <code>colorPlacingRequirements</code>,
     * <code>pointsRequirements</code>, and <code>symbolToCollect</code> attributes of
     * the <code>GoldCard</code> instance.
     *
     * @param builder the <code>GoldCard.Builder</code> instance that provides
     *                the attribute values for the new <code>GoldCard</code>
     */
    private GoldCard(@NotNull Builder builder) {
        super(builder);
        this.availableCorners = Maps.immutableEnumMap(builder.availableCorners);
        this.colorPlacingRequirements = Maps.immutableEnumMap(builder.colorPlacingRequirements);
        this.pointsRequirements = builder.pointsRequirements;
        this.symbolToCollect = builder.symbolToCollect;
    }

    /**
     * Returns the type of this <code>GoldCard</code>.
     * <p>
     * This method overrides the <code>getType</code> method in the superclass <code>PlayableCard</code>.
     * For GoldCard, this method will always return <code>PlayableCardType.GOLD</code>.
     *
     * @return <code>PlayableCardType.GOLD</code>
     */
    @Override
    @NotNull
    @Contract(pure = true)
    public PlayableCardType getType() {
        return PlayableCardType.GOLD;
    }

    /**
     * Checks whether a corner can be covered or not in this <code>GoldCard</code>.
     * <p>
     * This method overrides the <code>isAvailable</code> method in the superclass <code>PlayableCard</code>.
     * The availability of a corner in a <code>GoldCard</code> is determined by
     * the <code>availableCorners</code> attribute.
     *
     * @param corner the corner to check
     * @return true if the corner can be covered, false otherwise
     */
    @Override
    public boolean isAvailable(@NotNull Corner corner) {
        return Objects.requireNonNull(availableCorners.get(corner)).isAvailable();
    }

    /**
     * Gets the color requirements for placing this <code>GoldCard</code> on the field.
     * <p>
     * This method overrides the <code>getPlacingRequirements()</code> method in
     * the superclass <code>PlayableCard</code>.
     * The color requirements for a <code>GoldCard</code> are determined by
     * the <code>colorPlacingRequirements</code> attribute.
     *
     * @return a <code>Map</code> of keys colors and as values their requirements to
     * place on the field in int
     */
    @Override
    @NotNull
    public ImmutableMap<Color, Integer> getPlacingRequirements() {
        return colorPlacingRequirements;
    }

    /**
     * Gets the method needed to score this <code>GoldCard</code> points value.
     * <p>
     * This method overrides the <code>getPointsRequirements()</code> method in
     * the superclass <code>PlayableCard</code>.
     * The point requirements for a <code>GoldCard</code> are determined by
     * the <code>pointsRequirements</code> attribute.
     *
     * @return type of requirement needed to score points
     * @see PointsRequirementsType
     */
    @Override
    @NotNull
    public PointsRequirementsType getPointsRequirements() {
        return pointsRequirements;
    }

    /**
     * Gets the symbol to collect for scoring points with this <code>GoldCard</code>.
     * <p>
     * This method overrides the <code>getSymbolToCollect()</code> method in
     * the superclass <code>PlayableCard</code>.
     * If the <code>GoldCard</code> doesn't allow a symbol to collect,
     * it will return an empty <code>Optional</code>.
     *
     * @return an <code>Optional</code> containing the symbol to collect if there is any,
     * otherwise an empty <code>Optional</code>
     */
    @Override
    @NotNull
    public Optional<Symbol> getSymbolToCollect() {
        return Optional.ofNullable(symbolToCollect);
    }

    /**
     * Checks if there is an item in a corner of this <code>GoldCard</code>.
     * <p>
     * This method overrides the <code>checkItemCorner</code> method in
     * the superclass <code>PlayableCard</code>.
     * If the corner is empty or not available, it returns its <code>Availability</code>.
     *
     * @param corner the corner to check
     * @return a <code>CornerContainer</code> if there is an item,
     * otherwise it gives its <code>Availability</code>
     * @see CornerContainer
     * @see Availability
     */
    @Override
    @NotNull
    public CornerContainer checkItemCorner(@NotNull Corner corner) {
        return Objects.requireNonNull(availableCorners.get(corner));
    }

    /**
     * Builder class for creating instances of {@link GoldCard}.
     * This builder provides methods to set the required attributes
     * for the target object.
     */
    public static class Builder extends PlayableCard.Builder<GoldCard> {

        private final EnumMap<Corner, Availability> availableCorners;
        private final EnumMap<Color, Integer> colorPlacingRequirements;
        private PointsRequirementsType pointsRequirements;
        private @Nullable Symbol symbolToCollect;


        /**
         * Constructor for <code>Builder</code> that's needed for creation of a <code>GoldCard</code>.
         *
         * @param points       the point value of the card
         * @param primaryColor the color of the card
         * @throws IllegalBuildException if points are negative
         */
        public Builder(int id, int points, @NotNull Color primaryColor) throws IllegalBuildException {
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
         * @throws IllegalBuildException if placement requirements to place have a negative value
         */
        @Override
        @NotNull
        public GoldCard build() throws IllegalBuildException {
            if (this.pointsRequirements == null) throw new IllegalBuildException("No points requirements!");
            if (!Validator.nonNegativeValues(colorPlacingRequirements))
                throw new IllegalBuildException("Placing requirements cannot be less than 0!");
            return new GoldCard(this);
        }
    }
}
