package it.polimi.ingsw.am11.cards.playable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.cards.utils.CardIdentity;
import it.polimi.ingsw.am11.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.cards.utils.FieldCard;
import it.polimi.ingsw.am11.cards.utils.PointsCountable;
import it.polimi.ingsw.am11.cards.utils.enums.*;
import it.polimi.ingsw.am11.cards.utils.helpers.EnumMapUtils;
import it.polimi.ingsw.am11.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.players.PlayerField;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;


/**
 * Abstract class representing a playable card in the field.
 * <p>
 * Subclasses must be constructed using the provided {@link PlayableCard.Builder}
 * inner class to ensure consistent object creation.
 * <p>
 * This class contains methods to get the card's color, points, type,
 * placing requirements, points requirements, symbol to collect, and id.
 * <p>
 * It also contains methods to check if a corner is available
 * if there is an item in a corner, and if the card's color is equal to a given color.
 * <p>
 * The class also contains a nested <code>Builder</code> class for creating instances of <code>PlayableCard</code>.
 */
public abstract sealed class PlayableCard implements CardIdentity,
                                                     PointsCountable,
                                                     FieldCard permits GoldCard, ResourceCard {
    private static final ImmutableMap<Corner, Availability> retroCornerAvailability = Maps.immutableEnumMap(
            EnumMapUtils.Init(Corner.class, Availability.USABLE)
    );
    private final Color color;
    private final int points;
    private final int id;

    /**
     * The constructor for <code>PlayableCard</code> need to be called
     * from its subclasses to set color and point value.
     * <p>
     * Uses an inner static class <code>Builder</code> to get a new instance of the card
     *
     * @param builder A builder of its subclasses to set color and points values
     */
    protected PlayableCard(@NotNull Builder<?> builder) {
        this.color = builder.primaryColor;
        this.points = builder.cardPoints;
        this.id = builder.id;
    }

    /**
     * Checks retro corners availability.
     * <p>
     * As the physical game makes all <code>PlayableCard</code> retro corners available; it will always return true.
     *
     * @param corner the corner to check
     * @return always true
     */
    public static boolean isRetroAvailable(@NotNull Corner corner) {
        return retroCornerAvailability.get(corner) == Availability.USABLE;
    }

    /**
     * Gets the point value of this card
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
    public abstract boolean isFrontAvailable(@NotNull Corner corner);

    /**
     * Gets the number of colors in the field needed to place the card.
     * <p>
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
     * <p>
     * If the card doesn't give points it will return <code>PointsRequirementsType.CLASSIC</code>
     * as the card points value would be 0.
     *
     * @return type of requirement needed to score points
     * @see PointsRequirementsType
     */
    @Contract(pure = true)
    @NotNull
    public abstract PointsRequirementsType getPointsRequirements();

    /**
     * Check if there is an item in a corner.
     * <p>
     * If it is empty or not available, returns its <code>Availability</code>.
     *
     * @param corner the corner to check
     * @return an item if there is one otherwise gives its <code>Availability</code>
     * @see CornerContainer
     * @see Availability
     */
    @Contract(pure = true)
    @NotNull
    public abstract CornerContainer checkItemCorner(@NotNull Corner corner);

    /**
     * Getter method for the symbol to collect if there is any
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

    /**
     * Getter for card's id.
     * Each card should have a unique id.
     *
     * @return the id of the card
     */
    @Override
    public int getId() {
        return this.id;
    }


    /**
     * Counts the points obtained from placing the card on the field.
     *
     * @param playerField the player field to count points from
     * @return the points obtained
     */
    public int countPoints(@NotNull PlayerField playerField) {
        return this.points;
    }

    /**
     * This is an abstract <code>Builder</code> class for creating instances of <code>PlayableCard</code>.
     * <p>
     * The <code>Builder</code> follows the "Builder" design pattern.
     * It provides a way to construct a complex object step by step.
     * This <code>Builder</code> is abstract
     * and is meant
     * to be subclassed by concrete <code>Builder</code> classes for specific types of <code>PlayableCard</code>.
     * <p>
     * The <code>Builder</code> takes the id, points,
     * and primary color of the <code>PlayableCard</code> as parameters in its constructor.
     * These values are then used
     * to set the corresponding fields of the <code>PlayableCard</code> when the <code>build</code> method is called.
     * <p>
     * The <code>build</code> method is abstract and must be implemented by subclasses of this <code>Builder</code>.
     * It is responsible for creating the <code>PlayableCard</code> and ensuring that it is in a valid state.
     * <p>
     * The type parameter <code>T extends PlayableCard</code>,
     * meaning the <code>Builder</code> can be used to create any type of <code>PlayableCard</code>.
     *
     * @param <T> The specific type of <code>PlayableCard</code> that this <code>Builder</code> builds.
     */
    public abstract static class Builder<T extends PlayableCard> {
        private final int cardPoints;
        private final @NotNull Color primaryColor;

        private final int id;

        /**
         * The constructor for <code>PlayableCard.Builder</code> it sets as final its color, points and id.
         *
         * @param id           The id of the card unique from others
         * @param cardPoints   Points value of the card to create
         * @param primaryColor Color of the card to create
         * @throws IllegalCardBuildException if cardPoints are negative
         */
        protected Builder(int id, int cardPoints, @NotNull Color primaryColor) throws IllegalCardBuildException {
            if (cardPoints < 0) throw new IllegalCardBuildException("Points cannot be less than 0!");
            this.cardPoints = cardPoints;
            this.primaryColor = primaryColor;
            this.id = id;
        }

        public abstract Builder<T> hasIn(@NotNull Corner corner, @NotNull CornerContainer cornerContainer)
                throws IllegalCardBuildException;

        /**
         * Constructs a new instance of a subclass of <code>PlayableCard</code> using the
         * parameters set by the builder's methods.
         *
         * @return A fully constructed instance of a subclass of <code>PlayableCard</code>
         * @throws IllegalCardBuildException if the build is incomplete or impossible
         */
        public abstract T build() throws IllegalCardBuildException;
    }


}
