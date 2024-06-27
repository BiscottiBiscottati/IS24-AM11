package it.polimi.ingsw.am11.model.cards.starter;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.polimi.ingsw.am11.model.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.model.cards.utils.FieldCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.Availability;
import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.helpers.EnumMapUtils;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The <code>StarterCard</code> class represents a type of card played at the start of the game.
 * <p>
 * It implements the <code>FieldCard</code> and <code>CardIdentity</code> interfaces, indicating
 * that it can be used as a field card in the game and has a unique identity.
 * <p>
 * A <code>StarterCard</code> has several properties including an id, available corners on the front
 * and back of the card, and center colors on the front of the card. These properties are set during
 * the construction of the
 * <code>StarterCard</code> object using the Builder pattern.
 * <p>
 * The <code>StarterCard</code> class provides methods to check if a corner on the front of the card
 * is available, retrieve the <code>CornerContainer</code> for a given corner on the front of the
 * card, retrieve the color for a given corner on the back of the card, retrieve the set of center
 * colors on the front of the card, and retrieve the ID of the card.
 * <p>
 * The <code>StarterCard</code> class also includes a nested <code>Builder</code> class for
 * constructing a
 * <code>StarterCard</code> object.
 */
@SuppressWarnings("DataFlowIssue")
public final class StarterCard implements FieldCard {

    private final int id;
    private final @NotNull ImmutableMap<Corner, CornerContainer> availableCornersFront;
    private final @NotNull ImmutableMap<Corner, GameColor> availableColorCornerRetro;
    private final @NotNull ImmutableSet<GameColor> centerColorsFront;

    /**
     * Constructor for the <code>StarterCard</code> class.
     * <p>
     * This constructor takes a <code>Builder</code> object and uses it to set the properties of
     * the
     * <code>StarterCard</code>.
     * <p>
     * It sets the id of the card, the available corners on the front of the card, the available
     * color corners on the back of the card, and the center colors on the front of the card.
     * <p>
     * The available corners and color corners are set using <code>ImmutableEnumMaps</code>, and the
     * center colors are set using an <code>ImmutableEnumSet</code>.
     *
     * @param builder The <code>Builder</code> object containing the properties to be set on the
     *                <code>StarterCard</code>.
     */
    private StarterCard(@NotNull Builder builder) {
        this.id = builder.id;
        this.availableCornersFront = Maps.immutableEnumMap(builder.availableCornersFront);
        this.availableColorCornerRetro = Maps.immutableEnumMap(builder.availableColorCornerBack);
        this.centerColorsFront = Sets.immutableEnumSet(builder.centerColors);
    }

    /**
     * Checks if a corner on the front of the card is available.
     * <p>
     * This method takes a <code>Corner</code> object as a parameter and uses it to retrieve the
     * corresponding
     * <code>CornerContainer</code>.
     * <p>
     * It then calls the <code>isAvailable</code> method on the <code>CornerContainer</code> to
     * check if the corner is available.
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
     * This method takes a <code>Corner</code> object as a parameter and uses it to retrieve the
     * corresponding
     * <code>CornerContainer</code>.
     *
     * @param corner The corner for which to retrieve the <code>CornerContainer</code>.
     * @return The <code>CornerContainer</code> corresponding to the given corner.
     */
    public @NotNull CornerContainer checkFront(@NotNull Corner corner) {
        return Objects.requireNonNull(availableCornersFront.get(corner));
    }

    /**
     * Retrieves the color for a given corner on the back of the card.
     * <p>
     * This method takes a <code>Corner</code> object as a parameter and uses it to retrieve the
     * corresponding
     * <code>Color</code>.
     *
     * @param corner The corner for which to retrieve the <code>Color</code>.
     * @return The <code>Color</code> corresponding to the given corner.
     */
    @NotNull
    public GameColor checkRetroColorIn(@NotNull Corner corner) {
        return availableColorCornerRetro.get(corner);
    }

    /**
     * Retrieves the set of center colors on the front of the card.
     * <p>
     * This method returns the set of center colors on the front of the card.
     * <p>
     * The colors are represented as an <code>ImmutableSet</code> of <code>Color</code> enums.
     *
     * @return An <code>ImmutableSet</code> of <code>Color</code> enums representing the center
     * colors on the front of the card.
     */
    @NotNull
    public Set<GameColor> getCenterColorsFront() {
        return centerColorsFront;
    }

    @Override
    public boolean isColorEqual(@NotNull GameColor color) {
        return false;
    }

    @Override
    public boolean isAvailable(@NotNull Corner corner, boolean isRetro) {
        if (isRetro) return true;
        else return availableCornersFront.get(corner).isAvailable();
    }

    public @NotNull CornerContainer getItemCorner(@NotNull Corner corner, boolean isRetro) {
        if (isRetro) return availableColorCornerRetro.get(corner);
        else return availableCornersFront.get(corner);
    }

    public @NotNull Set<GameColor> getCenter(boolean isRetro) {
        if (isRetro) return EnumSet.noneOf(GameColor.class);
        else return centerColorsFront;
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

    @Override
    public int hashCode() {
        return Integer.hashCode(this.id);
    }

    @Override
    public boolean equals(@NotNull Object obj) {
        if (super.equals(obj)) return true;

        if (getClass() != obj.getClass()) return false;

        return this.id == ((StarterCard) obj).id;
    }

    /**
     * Builder class for the StarterCard.
     * <p>
     * This class is used to construct a StarterCard object using the Builder pattern. It allows for
     * the configuration of various properties of the StarterCard, such as its id, available corners
     * on the front, available color corners on the back, and center colors.
     * <p>
     * The Builder ensures that the StarterCard is in a valid state before it is constructed,
     * throwing an
     * <code>IllegalCardBuildException</code> if not all corners are covered.
     * <p>
     * Example usage:
     * {@snippet id = 'StarterCardBuilderExample' lang = 'java':
     * StarterCard card = new StarterCard.Builder(3)
     *        .hasItemFrontIn(Corner.DOWN_LX , Availability.USABLE)
     *        .hasColorRetroIn(Corner.TOP_RX , GameColor.GREEN)
     *        .hasCenterColors(GameColor.PURPLE)
     *        .build();
     *}
     */
    public static class Builder {
        private final int id;
        private final @NotNull EnumMap<Corner, CornerContainer> availableCornersFront;
        private final @NotNull EnumMap<Corner, GameColor> availableColorCornerBack;
        private final @NotNull EnumSet<GameColor> centerColors;

        /**
         * Constructor for the <code>Builder</code> class.
         * <p>
         * This constructor initializes the Builder with a given id and sets up the initial state
         * for the available corners on the front and back of the card, and the center colors.
         * <p>
         * The available corners on the front of the card are initialized with an
         * <code>EnumMap</code>, with all corners set to <code>NOT_USABLE</code>.
         * <p>
         * The available color corners on the back of the card are initialized with an empty
         * <code>EnumMap</code>. The center colors are initialized with an empty EnumSet.
         *
         * @param id The id to be set for the <code>Builder</code>.
         */
        public Builder(int id) {
            this.id = id;
            this.availableCornersFront = EnumMapUtils.init(Corner.class, Availability.NOT_USABLE);
            this.availableColorCornerBack = new EnumMap<>(Corner.class);
            this.centerColors = EnumSet.noneOf(GameColor.class);
        }

        /**
         * Sets the item for a given corner on the front of the card.
         * <p>
         * This method takes a <code>Corner</code> object and a <code>CornerContainer</code> object
         * as parameters. It uses the <code>Corner</code> object to identify the corner on the front
         * of the card, and the
         * <code>CornerContainer</code> object to set the item for that corner.
         * <p>
         * The item can be either an <code>Availability</code> or a <code>Color</code>. The item is
         * set in the available corners on the front of the card.
         * <p>
         * If the item is neither an <code>Availability</code> nor a <code>Color</code>, an
         * <code>IllegalCardBuildException</code> is thrown.
         *
         * @param corner The corner on the front of the card for which to set the item.
         * @param item   The item to be set for the given corner.
         * @return This <code>Builder</code> instance (for chaining).
         * @throws IllegalCardBuildException If the item is neither an <code>Availability</code> nor
         *                                   a
         *                                   <code>Color</code>.
         */
        public @NotNull Builder hasItemFrontIn(@NotNull Corner corner,
                                               @NotNull CornerContainer item)
        throws IllegalCardBuildException {
            switch (item) {
                case Availability availability -> availableCornersFront.put(corner, availability);
                case GameColor color -> availableCornersFront.put(corner, color);
                default -> throw new IllegalCardBuildException("Illegal Item for starter:" + item);
            }
            return this;
        }

        /**
         * Sets the color for a given corner on the back of the card.
         * <p>
         * This method takes a <code>Corner</code> object and a <code>Color</code> object as
         * parameters. It uses the
         * <code>Corner</code> object to identify the corner on the back of the card, and the
         * <code>Color</code> object
         * to set the color for that corner.
         * <p>
         * The color is set in the available corner on the back of the card.
         *
         * @param corner The corner on the back of the card for which to set the color.
         * @param color  The color to be set for the given corner.
         * @return This <code>Builder</code> instance (for chaining).
         */
        public @NotNull Builder hasColorRetroIn(@NotNull Corner corner, @NotNull GameColor color) {
            availableColorCornerBack.put(corner, color);
            return this;
        }

        /**
         * Sets the center colors on the front of the card.
         * <p>
         * This method takes a <code>Set</code> of <code>Color</code> objects as a parameter. It
         * adds all the colors from the set to the center colors on the front of the card.
         * <p>
         * The colors are represented as an <code>EnumSet</code> of <code>Color</code> enums.
         *
         * @param colors The set of colors to be added to the center of the card.
         * @return This <code>Builder</code> instance (for chaining).
         */
        public @NotNull Builder hasCenterColors(@NotNull Set<GameColor> colors) {
            centerColors.addAll(colors);
            return this;
        }

        /**
         * Sets a center color on the front of the card.
         * <p>
         * This method takes a <code>Color</code> object as a parameter. It adds the color to the
         * center colors on the front of the card.
         * <p>
         * The colors are represented as an <code>EnumSet</code> of <code>Color</code> enums.
         *
         * @param color The color to be added to the center of the card.
         * @return This <code>Builder</code> instance (for chaining).
         */
        public @NotNull Builder hasCenterColor(@NotNull GameColor color) {
            centerColors.add(color);
            return this;
        }

        /**
         * Builds and returns a StarterCard object.
         * <p>
         * This method first checks if all corners on the back of the card are covered.
         * <p>
         * If they are, it creates a new <code>StarterCard</code> object with the current state of
         * the
         * <code>Builder</code> and returns it.
         * <p>
         * If not all corners are covered, it throws an <code>IllegalCardBuildException</code>.
         *
         * @return A new <code>StarterCard</code> object with the current state of the
         * <code>Builder</code>.
         * @throws IllegalCardBuildException If not, all corners on the back of the card are
         *                                   covered.
         */
        @NotNull
        public StarterCard build() throws IllegalCardBuildException {
            if (checkAllBackCornerCovered()) return new StarterCard(this);
            else throw new IllegalCardBuildException("Not all Corners Covered");
        }

        private boolean checkAllBackCornerCovered() {
            return Arrays.stream(Corner.values())
                         .filter(key -> ! availableColorCornerBack.containsKey(key))
                         .noneMatch(e -> true);
        }
    }
}
