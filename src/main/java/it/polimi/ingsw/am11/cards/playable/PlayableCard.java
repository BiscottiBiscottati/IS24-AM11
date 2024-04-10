package it.polimi.ingsw.am11.cards.playable;

import it.polimi.ingsw.am11.cards.utils.CardIdentity;
import it.polimi.ingsw.am11.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.cards.utils.FieldCard;
import it.polimi.ingsw.am11.cards.utils.enums.*;
import it.polimi.ingsw.am11.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.players.PlayerField;
import it.polimi.ingsw.am11.players.Position;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.Set;


/**
 * Abstract class representing a playable card in the field.
 * <p>
 * Subclasses must be constructed using the provided {@link PlayableCard.Builder Builder}
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
                                                     FieldCard permits GoldCard, ResourceCard {
    private final Color color;
    private final int points;
    private final int id;

    //TODO may need cleanup of methods

    /**
     * Constructs a new instance of <code>PlayableCard</code> using the provided <code>Builder</code>.
     * <p>
     * This constructor is protected.
     * This is to ensure that <code>PlayableCard</code> instances are always created using a <code>Builder</code>.
     * <p>
     * The constructor takes a <code>Builder</code> as a parameter and uses it to set the color,
     * points, and id of the <code>PlayableCard</code>.
     * These values are final and can’t be changed after the <code>PlayableCard</code> is constructed.
     *
     * @param builder The <code>Builder</code> used to construct the PlayableCard.
     *                It must be a subclass of <code>Builder</code>
     *                that is specific to the type of <code>PlayableCard</code> being created.
     */
    protected PlayableCard(@NotNull Builder<?> builder) {
        this.color = builder.primaryColor;
        this.points = builder.cardPoints;
        this.id = builder.id;
    }

    /**
     * Getter for card's id.
     * Each card must have a unique id.
     *
     * @return the id of the card
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * Retrieves the point value of this <code>PlayableCard</code>.
     *
     * @return The point value of this <code>PlayableCard</code>.
     */
    @Contract(pure = true)
    public int getPoints() {
        return points;
    }

    /**
     * Gets the color of this <code>PlayableCard</code>
     *
     * @return color of the <code>PlayableCard</code>
     */
    @Contract(pure = true)
    @NotNull
    public Color getColor() {
        return color;
    }

    public Set<Color> getCenter(boolean isRetro) {
        if (isRetro) return Set.of(this.color);
        else return Set.of();
    }

    /**
     * Gets the type of this <code>PlayableCard</code>.
     *
     * @return the type of playable card
     */
    @Contract(pure = true)
    @NotNull
    public abstract PlayableCardType getType();

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) return true;

        if (getClass() != obj.getClass()) return false;

        return this.id == ((PlayableCard) obj).id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.id);
    }

    /**
     * Checks whether a corner on the front side of a <code>PlayableCard</code> can be covered or not.
     *
     * @param corner The corner to check for cover-ability.
     * @return true if the specified corner can be covered, false otherwise.
     */
    @Contract(pure = true)
    public abstract boolean isFrontAvailable(@NotNull Corner corner);

    @Override
    public abstract boolean isAvailable(@NotNull Corner corner, boolean isRetro);

    /**
     * Gets the number of colors in the field needed to place the card.
     * <p>
     * For <code>ResourceCard</code> all color requirements will be 0 as they have
     * no requirements to place.
     *
     * @return a <code>Map</code> of keys colors and as values their requirements to place on the field in int
     */
    @Contract(pure = true)
    public abstract @NotNull Map<Color, Integer> getPlacingRequirements();

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
     * Checks if there is an item in a specified corner of the <code>PlayableCard</code>.
     * <p>
     * The method returns a <code>CornerContainer</code> object representing the item or its <code>Availability</code>.
     * <p>
     * The method is marked as pure, meaning it doesn’t modify the state of the object and only returns a value.
     *
     * @param corner The corner to check for an item.
     * @return A <code>CornerContainer</code> object on the specified corner.
     * @see CornerContainer
     * @see Availability
     */
    @Contract(pure = true)
    @NotNull
    public abstract CornerContainer checkItemCorner(@NotNull Corner corner);

    public abstract CornerContainer checkItemCorner(@NotNull Corner corner, boolean isRetro);

    /**
     * Getter method for the symbol to collect if there is any
     * <p>
     * A <code>Symbol</code> is present when the card <code>PointsRequirementsType</code> is <code>SYMBOLS</code>,
     * otherwise it will return an empty <code>Optional</code>.
     *
     * @return a symbol if the cards permits a symbol to collect otherwise an empty <code>Optional</code>
     */
    @Contract(pure = true)
    @NotNull
    public abstract Optional<Symbol> getSymbolToCollect();

    /**
     * Checks if the <code>PlayableCard</code> color is the same as the given color.
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
     * Counts the points obtained from placing the card on the field.
     *
     * @param playerField    the player field to count points from
     * @param positionOfCard the position of the card to be placed on the field
     * @return the points obtained
     */
    public int countPoints(@NotNull PlayerField playerField, Position positionOfCard) {
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

        /**
         * Specifies the item to be placed in a given corner of the <code>PlayableCard</code> being built.
         * <p>
         * This method is abstract and must be implemented by subclasses of <code>Builder</code>.
         * <p>
         * If the specified corner can’t contain the provided item,
         * the method throws an <code>IllegalCardBuildException</code>.
         *
         * @param corner          The corner where the item should be placed.
         * @param cornerContainer The item to be placed in the specified corner.
         * @return The <code>Builder</code> instance, allowing for method chaining.
         * @throws IllegalCardBuildException if the item can’t be placed in the specified corner.
         */
        public abstract Builder<T> hasIn(@NotNull Corner corner, @NotNull CornerContainer cornerContainer)
                throws IllegalCardBuildException;

        /**
         * Constructs a new instance of a subclass of <code>PlayableCard</code> using the
         * parameters set by the builder's methods.
         * <p>
         * The method uses the parameters set by the builder's methods to construct the <code>PlayableCard</code>.
         * These parameters include the color, points, id,
         * and any items placed in the corners of the <code>PlayableCard</code>.
         * <p>
         * If the build is incomplete or impossible
         * (for example, if an item is placed in a corner that can’t contain it),
         * the method throws an <code>IllegalCardBuildException</code>.
         *
         * @return A fully constructed instance of a subclass of <code>PlayableCard</code>
         * @throws IllegalCardBuildException if the build is incomplete or impossible
         */
        public abstract T build() throws IllegalCardBuildException;
    }


}
