package it.polimi.ingsw.am11.cards.objective;

import it.polimi.ingsw.am11.cards.utils.CardIdentity;
import it.polimi.ingsw.am11.cards.utils.CardPattern;
import it.polimi.ingsw.am11.cards.utils.Item;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.ObjectiveCardType;
import it.polimi.ingsw.am11.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.players.field.PlayerField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

/**
 * This is an abstract class representing an objective card in the game.
 * <p>
 * An <code>ObjectiveCard</code> is a type of card that players
 * can use to achieve certain objectives in the game.
 * <p>
 * Each <code>ObjectiveCard</code> has a certain number of points and an ID.
 * <p>
 * This class implements the <code>CardIdentity</code> and <code>PointsCountable</code> interfaces,
 * meaning it has methods for getting the ID of the card and the points it provides.
 * <p>
 * The actual implementation of the card's functionality is left to subclasses.
 * These subclasses must provide implementations for the abstract methods defined in this class.
 */
public abstract class ObjectiveCard implements CardIdentity {
    private final int points;

    private final int id;

    /**
     * Constructor for the <code>ObjectiveCard</code> class.
     * <p>
     * This constructor takes a <code>Builder</code> object as a parameter.
     * It uses the <code>Builder</code> to set the points and id fields of the <code>ObjectiveCard</code>.
     * <p>
     * The points and id fields are final, meaning they cannot be
     * changed after the <code>ObjectiveCard</code> is created.
     * <p>
     * This ensures the immutability of <code>ObjectiveCard</code> objects.
     *
     * @param builder The <code>Builder</code> object from which
     *                to get the points and id for the <code>ObjectiveCard</code>.
     */
    protected ObjectiveCard(@NotNull Builder<?> builder) {
        this.points = builder.points;
        this.id = builder.id;
    }

    /**
     * Retrieves the ID of this <code>ObjectiveCard</code>.
     * <p>
     * This method returns the ID of the <code>ObjectiveCard</code>.
     * <p>
     * The ID is a unique identifier for each card.
     * <p>
     * It is set when the card is created and cannot be changed afterward.
     *
     * @return The ID of this <code>ObjectiveCard</code>.
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * Retrieves the points of this <code>ObjectiveCard</code>.
     * <p>
     * This method returns the points of the <code>ObjectiveCard</code>.
     * The points represent the score a player can achieve by fulfilling the objective of the card.
     * <p>
     * The points are set when the card is created and cannot be changed afterward.
     *
     * @return The points of this <code>ObjectiveCard</code>.
     */
    public int getPoints() {
        return points;
    }


    // TODO: We can delete getSymbolRequirements and get ColorRequirements and use hasItemRequirements instead

    /**
     * Retrieves the symbol requirements of this <code>ObjectiveCard</code>.
     * <p>
     * This method returns an <code>Map</code>
     * where the keys are <code>Symbols</code>
     * and the values are <code>Integers</code>.
     * <p>
     * Each entry in the map represents a requirement for a specific symbol,
     * and the associated integer is the quantity of that symbol required.
     * <p>
     * The symbol requirements are set when the card is created and cannot be changed afterward.
     * <p>
     * Note: the returned <code>Map</code> is implemented
     * with {@link com.google.common.collect.ImmutableMap ImmutableMap}.
     *
     * @return A <code>Map</code> of the symbol requirements of this <code>ObjectiveCard</code>.
     */
    public abstract @NotNull Map<Symbol, Integer> getSymbolRequirements();

    /**
     * Retrieves the color requirements of this <code>ObjectiveCard</code>.
     * <p>
     * This method returns an <code>Map</code>
     * where the keys are <code>Colors</code>
     * and the values are <code>Integers</code>.
     * <p>
     * Each entry in the map represents a requirement for a specific color,
     * and the associated integer is the quantity of that color required.
     * <p>
     * The color requirements are set when the card is created and cannot be changed afterward.
     * <p>
     * Note: the returned <code>Map</code> is implemented
     * with {@link com.google.common.collect.ImmutableMap ImmutableMap}.
     *
     * @return A <code>Map</code> of the color requirements of this <code>ObjectiveCard</code>.
     */
    public abstract @NotNull Map<Color, Integer> getColorRequirements();

    /**
     * Checks if the provided item meets the requirements of this <code>ObjectiveCard</code>.
     * <p>
     * This method takes an <code>Item</code> as a parameter,
     * which can be either a <code>Color</code> or a <code>Symbol</code>.
     * It then checks the requirements of the <code>ObjectiveCard</code> for that item.
     * <p>
     * If the item is a Color, it checks the color requirements of the ObjectiveCard.
     * If the item is a Symbol, it checks the symbol requirements of the ObjectiveCard.
     * <p>
     * The method returns the quantity of the item required by the ObjectiveCard.
     *
     * @param item The item to check the requirements for.
     *             This can be either a <code>Color</code> or a <code>Symbol</code>.
     * @return The quantity of the item required by the <code>ObjectiveCard</code>,
     * or null if the item is not a requirement.
     */
    public int hasItemRequirements(@NotNull Item item) {
        switch (item) {
            case Color color -> {
                return Objects.requireNonNull(getColorRequirements().get(color));
            }
            case Symbol symbol -> {
                return Objects.requireNonNull(getSymbolRequirements().get(symbol));
            }
        }
    }

    /**
     * Retrieves the type of this <code>ObjectiveCard</code>.
     * <p>
     * This method returns the <code>ObjectiveCardType</code> of the <code>ObjectiveCard</code>.
     * The <code>ObjectiveCardType</code> is an enum that represents the type of the <code>ObjectiveCard</code>.
     * <p>
     * The type of the <code>ObjectiveCard</code> is set when the card is created and cannot be changed afterward.
     *
     * @return The <code>ObjectiveCardType</code> of this <code>ObjectiveCard</code>.
     */
    @NotNull
    public abstract ObjectiveCardType getType();

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) return true;

        if (getClass() != obj.getClass()) return false;

        return this.id == ((ObjectiveCard) obj).id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.id);
    }

    public abstract int countPoints(PlayerField playerField);

    // TODO: up for discussion if to use this
    @Nullable
    public CardPattern getPattern() {
        return null;
    }

    /**
     * This is an abstract <code>Builder</code> class for creating instances of <code>ObjectiveCard</code>.
     * <p>
     * The <code>Builder</code> follows the "Builder" design pattern.
     * It provides a way to construct a complex object step by step.
     * This <code>Builder</code> is abstract
     * and is meant
     * to be subclassed by concrete <code>Builder</code> classes for specific types of <code>ObjectiveCard</code>.
     * <p>
     * The <code>Builder</code> takes the id
     * and points of the <code>ObjectiveCard</code> as parameters in its constructor.
     * These values are then used
     * to set the corresponding fields of the <code>ObjectiveCard</code> when the <code>build</code>
     * method is called.
     * <p>
     * The <code>build</code> method is abstract and must be implemented by subclasses of this <code>Builder</code>.
     * It is responsible for creating the <code>ObjectiveCard</code> and ensuring that it is in a valid state.
     * <p>
     * The type parameter <code>T</code> extends <code>ObjectiveCard</code>,
     * meaning the <code>Builder</code> can be used to create any type of <code>ObjectiveCard</code>.
     *
     * @param <T> The specific type of <code>ObjectiveCard</code> that this <code>Builder</code> builds.
     */
    public static abstract class Builder<T extends ObjectiveCard> {
        private final int points;

        private final int id;

        /**
         * Constructor for the <code>Builder</code> class.
         * <p>
         * This constructor takes the id and points of the <code>ObjectiveCard</code> as parameters.
         * These values are then used
         * to set the corresponding fields of the <code>ObjectiveCard</code> when the <code>build</code>
         * method is called.
         * <p>
         * The points must be greater than 0, otherwise an <code>IllegalArgumentException</code> is thrown.
         *
         * @param id     The unique identifier for the ObjectiveCard to be built.
         * @param points The points of the ObjectiveCard to be built. Must be greater than 0.
         * @throws IllegalArgumentException if points are less than or equal to 0.
         */
        protected Builder(int id, int points) throws IllegalArgumentException {
            if (points > 0) this.points = points;
            else throw new IllegalArgumentException("points cannot be less than 0!");
            this.id = id;
        }

        /**
         * Builds an instance of <code>T</code>, which extends <code>ObjectiveCard</code>.
         * <p>
         * This method is responsible for creating the
         * <code>ObjectiveCard</code> and ensuring that it is in a valid state.
         * It uses the id and points provided to the <code>Builder</code>'s constructor
         * to set the corresponding fields of the <code>ObjectiveCard</code>.
         * <p>
         * This method is abstract and must be implemented by subclasses of this <code>Builder</code>.
         * The specific implementation will depend on the specific type of <code>ObjectiveCard</code> being built.
         * <p>
         * Note:
         * This method can throw an <code>IllegalCardBuildException</code>
         * if the <code>ObjectiveCard</code> cannot be built in a valid state.
         *
         * @return An instance of <code>T</code>, which extends <code>ObjectiveCard</code>.
         * @throws IllegalCardBuildException if the <code>ObjectiveCard</code> cannot be built in a valid state.
         */
        public abstract T build() throws IllegalCardBuildException;
    }

}
