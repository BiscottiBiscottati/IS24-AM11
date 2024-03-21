package it.polimi.ingsw.am11.cards.playable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.cards.utils.*;
import it.polimi.ingsw.am11.exceptions.IllegalBuildException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;


public abstract sealed class PlayableCard implements FieldCard permits GoldCard, ResourceCard {
    private static final ImmutableMap<Corner, Availability> retroCorners = Maps.immutableEnumMap(
            EnumMapUtils.Init(Corner.class, Availability.USABLE)
    );
    private final Color color;
    private final int points;

    /**
     * The constructor for <code>PlayableCard</code> need to be called
     * from its subclasses to set color and points value.
     * Uses an inner static class <code>Builder</code> to get a new instance of the card
     *
     * @param builder A builder of its subclasses to set color and points values
     */
    protected PlayableCard(@NotNull Builder builder) {
        this.color = builder.primaryColor;
        this.points = builder.cardPoints;
    }

    public static boolean isRetroAvailable(@NotNull Corner corner) {
        return Objects.requireNonNull(retroCorners.get(corner)).isAvailable();
    }

    /**
     * Gets the points value of this card
     *
     * @return points value of the card
     */
    @Contract(pure = true)
    public int getPoints() {
        return points;
    }

    /**
     * Gets the color of this card
     *
     * @return color of the card
     */
    @Contract(pure = true)
    public Color getColor() {
        return color;
    }

    /**
     * Gets the type of this <code>PlayableCard</code>.
     *
     * @return the type of playable card
     * @see PlayableCardType
     */
    @Contract(pure = true)
    @NotNull
    public abstract PlayableCardType getType();

    /**
     * Checks whether a corner can be covered or not
     *
     * @param corner the corner to check
     * @return true if available false otherwise
     */
    @Contract(pure = true)
    public abstract boolean isAvailable(@NotNull Corner corner);

    /**
     * Gets the number of colors in the field needed to place the card.
     * For <code>ResourceCard</code> all color requirements will be 0 as they have
     * no requirements to place.
     *
     * @return a <code>Map</code> of keys colors and as values their requirements to place on the field in int
     */
    @Contract(pure = true)
    @NotNull
    public abstract ImmutableMap<Color, Integer> getPlacingRequirements();

    /**
     * Gets the method needed to score this card points value.
     * If the card doesn't give points it will return <code>PointsRequirementsType.CLASSIC</code>
     * as the card points value would be 0
     *
     * @return type of requirement needed to score points
     * @see PointsRequirementsType
     */
    @Contract(pure = true)
    @NotNull
    public abstract PointsRequirementsType getPointsRequirements();

    /**
     * Check if there's an item in a corner.
     * If it's empty or not available returns its <code>Availability</code>
     *
     * @param corner the corner to check
     * @return an item if there's one otherwise gives its <code>Availability</code>
     * @see CornerContainer
     * @see Availability
     */
    @Contract(pure = true)
    @NotNull
    public abstract CornerContainer checkItemCorner(@NotNull Corner corner);

    /**
     * Getter method for the symbol to collect if there's any
     *
     * @return a symbol if the cards permits a symbol to collect otherwise an empty <code>Optional</code>
     */
    @Contract(pure = true)
    @NotNull
    public abstract Optional<Symbol> getSymbolToCollect();

    /**
     * Checks if the <code>PlayableCard</code> color is the same as the parameter
     *
     * @param color The <code>color</code> to check
     * @return <code>true</code> if this card's color is equal to <code>color</code> param otherwise <code>false</code>
     */
    @Contract(pure = true)
    @Override
    public boolean isColorEqual(Color color) {
        return this.color == color;
    }

    @Override
    public boolean equals(Object color) {
        if (color instanceof Color) return this.color == color;
        else return super.equals(color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.color, this.points);
    }

    /**
     * The builder for creating Playable cards needs to be inherited to subclasses.
     */
    public abstract static class Builder {
        private final int cardPoints;
        private final @NotNull Color primaryColor;

        /**
         * The constructor for <code>PlayableCard.Builder</code> it sets as final its color and points
         *
         * @param cardPoints   Points value of the card to create
         * @param primaryColor Color of the card to create
         * @throws IllegalBuildException if cardPoints are negative
         */
        protected Builder(int cardPoints, @NotNull Color primaryColor) throws IllegalBuildException {
            if (cardPoints < 0) throw new IllegalBuildException("Points cannot be less than 0!");
            this.cardPoints = cardPoints;
            this.primaryColor = primaryColor;
        }

        /**
         * Method that needs to be called to finalize the build after setting all its values
         *
         * @return A new instance of PlayableCard
         * @throws IllegalBuildException if the build is incomplete or impossible
         */
        public abstract PlayableCard build() throws IllegalBuildException;
    }


}
