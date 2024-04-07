package it.polimi.ingsw.am11.cards.starter;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.polimi.ingsw.am11.cards.utils.CardIdentity;
import it.polimi.ingsw.am11.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.cards.utils.FieldCard;
import it.polimi.ingsw.am11.cards.utils.enums.Availability;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.cards.utils.helpers.EnumMapUtils;
import it.polimi.ingsw.am11.exceptions.IllegalCardBuildException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class StarterCard implements FieldCard, CardIdentity {

    private final int id;
    private final ImmutableMap<Corner, CornerContainer> availableCornersFront;
    private final ImmutableMap<Corner, Color> availableColorCornerBack;
    private final ImmutableSet<Color> centerColorsFront;

    /**
     * Constructor for the <code>StarterCard</code> class.
     * <p>
     * This constructor takes a <code>Builder</code> object
     * and uses it to set the properties of the <code>StarterCard</code>.
     * <p>
     * It sets the id of the card, the available corners on the front of the card,
     * the available color corners on the back of the card,
     * and the center colors on the front of the card.
     * <p>
     * The available corners and color corners are set using <code>ImmutableEnumMaps</code>,
     * and the center colors are set using an <code>ImmutableEnumSet</code>.
     *
     * @param builder The <code>Builder</code> object containing
     *                the properties to be set on the <code>StarterCard</code>.
     */
    private StarterCard(@NotNull Builder builder) {
        this.id = builder.id;
        this.availableCornersFront = Maps.immutableEnumMap(builder.availableCornersFront);
        this.availableColorCornerBack = Maps.immutableEnumMap(builder.availableColorCornerBack);
        this.centerColorsFront = Sets.immutableEnumSet(builder.centerColors);
    }

    /**
     * Checks if a corner on the front of the card is available.
     * <p>
     * This method takes a <code>Corner</code> object as a parameter
     * and uses it to retrieve the corresponding <code>CornerContainer</code>.
     * <p>
     * It then calls the <code>isAvailable</code> method on the <code>CornerContainer</code>
     * to check if the corner is available.
     *
     * @param corner The corner to check.
     * @return true if the corner is available, false otherwise.
     */
    public boolean isFrontAvail(@NotNull Corner corner) {
        return Objects.requireNonNull(availableCornersFront.get(corner)).isAvailable();
    }

    /**
     * Retrieves the <code>CornerContainer</code> for a given corner on the front of the card.
     * <p>
     * This method takes a <code>Corner</code> object as a parameter
     * and uses it to retrieve the corresponding <code>CornerContainer</code>.
     *
     * @param corner The corner for which to retrieve the <code>CornerContainer</code>.
     * @return The <code>CornerContainer</code> corresponding to the given corner.
     */
    public CornerContainer getFront(@NotNull Corner corner) {
        return Objects.requireNonNull(availableCornersFront.get(corner));
    }

    /**
     * Retrieves the color for a given corner on the back of the card.
     * <p>
     * This method takes a <code>Corner</code> object as a parameter
     * and uses it to retrieve the corresponding <code>Color</code>.
     *
     * @param corner The corner for which to retrieve the <code>Color</code>.
     * @return The <code>Color</code> corresponding to the given corner.
     */
    @NotNull
    public Color getRetroColorIn(@NotNull Corner corner) {
        return Objects.requireNonNull(availableColorCornerBack.get(corner));
    }

    /**
     * Retrieves the set of center colors on the front of the card.
     * <p>
     * This method returns the set of center colors on the front of the card.
     * <p>
     * The colors are represented as an <code>ImmutableSet</code> of <code>Color</code> enums.
     *
     * @return An <code>ImmutableSet</code> of <code>Color</code> enums
     * representing the center colors on the front of the card.
     */
    @NotNull
    public Set<Color> getCenterColorsFront() {
        return centerColorsFront;
    }

    @Override
    public boolean isColorEqual(Color color) {
        return false;
    }

    /**
     * Retrieves the ID of the card.
     * <p>
     * It returns the ID of the card, which is an integer value.
     *
     * @return The ID of the card.
     */
    @Contract(pure = true)
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * Builder class for the StarterCard.
     * <p>
     * This class is used to construct a StarterCard object using the Builder pattern.
     * It allows for the configuration of various properties of the StarterCard,
     * such as its id, available corners on the front, available color corners on the back, and center colors.
     * <p>
     * The Builder ensures that the StarterCard is in a valid state before it is constructed,
     * throwing an <code>IllegalCardBuildException</code> if not all corners are covered.
     * <p>
     * Example usage:
     * {@snippet id = 'StarterCardBuilderExample' lang = 'java':
     * StarterCard card = new StarterCard.Builder(id)
     *                                   .hasItemFrontIn(corner,item)
     *                                   .hasColorRetroIn(corner,color)
     *                                   .hasCenterColors(colors)
     *                                   .build();
     *}
     */
    public static class Builder {
        private final int id;
        private final EnumMap<Corner, CornerContainer> availableCornersFront;
        private final EnumMap<Corner, Color> availableColorCornerBack;
        private final EnumSet<Color> centerColors;

        /**
         * Constructor for the <code>Builder</code> class.
         * <p>
         * This constructor initializes the Builder with a given id and sets up
         * the initial state for the available corners on the front and back of the card, and the center colors.
         * <p>
         * The available corners on the front of the card are initialized with an <code>EnumMap</code>,
         * with all corners set to <code>NOT_USABLE</code>.
         * <p>
         * The available color corners on the back of the card are initialized with an empty <code>EnumMap</code>.
         * The center colors are initialized with an empty EnumSet.
         *
         * @param id The id to be set for the <code>Builder</code>.
         */
        public Builder(int id) {
            this.id = id;
            this.availableCornersFront = EnumMapUtils.Init(Corner.class, Availability.NOT_USABLE);
            this.availableColorCornerBack = new EnumMap<>(Corner.class);
            this.centerColors = EnumSet.noneOf(Color.class);
        }

        private boolean checkAllBackCornerCovered() {
            return Arrays.stream(Corner.values())
                         .filter(key -> !availableColorCornerBack.containsKey(key))
                         .noneMatch(e -> true);
        }

        /**
         * Sets the item for a given corner on the front of the card.
         * <p>
         * This method takes a <code>Corner</code> object and a <code>CornerContainer</code> object as parameters.
         * It uses the <code>Corner</code> object to identify the corner on the front of the card,
         * and the <code>CornerContainer</code> object to set the item for that corner.
         * <p>
         * The item can be either an <code>Availability</code> or a <code>Color</code>.
         * The item is set in the available corners on the front of the card.
         * <p>
         * If the item is neither an <code>Availability</code> nor a <code>Color</code>,
         * an <code>IllegalCardBuildException</code> is thrown.
         *
         * @param corner The corner on the front of the card for which to set the item.
         * @param item   The item to be set for the given corner.
         * @return This <code>Builder</code> instance (for chaining).
         * @throws IllegalCardBuildException If the item is neither
         *                                   an <code>Availability</code> nor a <code>Color</code>.
         */
        public @NotNull Builder hasItemFrontIn(@NotNull Corner corner, @NotNull CornerContainer item)
                throws IllegalCardBuildException {
            switch (item) {
                case Availability availability -> availableCornersFront.put(corner, availability);
                case Color color -> availableCornersFront.put(corner, color);
                default -> throw new IllegalCardBuildException("Illegal Item for starter:" + item);
            }
            return this;
        }

        /**
         * Sets the color for a given corner on the back of the card.
         * <p>
         * This method takes a <code>Corner</code> object and a <code>Color</code> object as parameters.
         * It uses the <code>Corner</code> object to identify the corner on the back of the card,
         * and the <code>Color</code> object to set the color for that corner.
         * <p>
         * The color is set in the available corner on the back of the card.
         *
         * @param corner The corner on the back of the card for which to set the color.
         * @param color  The color to be set for the given corner.
         * @return This <code>Builder</code> instance (for chaining).
         */
        public @NotNull Builder hasColorRetroIn(@NotNull Corner corner, @NotNull Color color) {
            availableColorCornerBack.put(corner, color);
            return this;
        }

        /**
         * Sets the center colors on the front of the card.
         * <p>
         * This method takes a <code>Set</code> of <code>Color</code> objects as a parameter.
         * It adds all the colors from the set to the center colors on the front of the card.
         * <p>
         * The colors are represented as an <code>EnumSet</code> of <code>Color</code> enums.
         *
         * @param colors The set of colors to be added to the center of the card.
         * @return This <code>Builder</code> instance (for chaining).
         */
        public @NotNull Builder hasCenterColors(@NotNull Set<Color> colors) {
            centerColors.addAll(colors);
            return this;
        }


        /**
         * Sets a center color on the front of the card.
         * <p>
         * This method takes a <code>Color</code> object as a parameter.
         * It adds the color to the center colors on the front of the card.
         * <p>
         * The colors are represented as an <code>EnumSet</code> of <code>Color</code> enums.
         *
         * @param color The color to be added to the center of the card.
         * @return This <code>Builder</code> instance (for chaining).
         */
        public @NotNull Builder hasCenterColor(@NotNull Color color) {
            centerColors.add(color);
            return this;
        }

        /**
         * Builds and returns a StarterCard object.
         * <p>
         * This method first checks if all corners on the back of the card are covered.
         * <p>
         * If they are, it creates a new <code>StarterCard</code> object
         * with the current state of the <code>Builder</code> and returns it.
         * <p>
         * If not all corners are covered, it throws an <code>IllegalCardBuildException</code>.
         *
         * @return A new <code>StarterCard</code> object with the current state of the <code>Builder</code>.
         * @throws IllegalCardBuildException If not all corners on the back of the card are covered.
         */
        @NotNull
        public StarterCard build() throws IllegalCardBuildException {
            if (checkAllBackCornerCovered()) return new StarterCard(this);
            else throw new IllegalCardBuildException("Not all Corners Covered");
        }
    }
}
