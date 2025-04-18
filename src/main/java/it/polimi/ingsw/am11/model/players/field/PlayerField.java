package it.polimi.ingsw.am11.model.players.field;

import it.polimi.ingsw.am11.model.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.model.cards.starter.StarterCard;
import it.polimi.ingsw.am11.model.cards.utils.FieldCard;
import it.polimi.ingsw.am11.model.cards.utils.Item;
import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardPlacingException;
import it.polimi.ingsw.am11.model.players.utils.CardContainer;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.model.utils.memento.FieldMemento;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * The PlayerField class represents the field of a player in the game.
 * <p>
 * It manages the positioning and exposure of cards on the field.
 * <p>
 * The class contains two main parts: an ExposedItemManager and a PositionManager.
 * <p>
 * The ExposedItemManager is responsible for managing the items exposed in the field.
 * <p>
 * The PositionManager is responsible for managing the positions of the cards on the field.
 * <p>
 * The class provides methods for placing cards on the field, checking the availability of
 * positions, getting the number of specific items exposed on the field, and checking if a card is
 * present on the field.
 * <p>
 * It also provides methods for saving and loading the state of the field.
 */
public class PlayerField {

    private final @NotNull ExposedItemManager itemManager;
    private final @NotNull PositionManager positionManager;

    public PlayerField() {
        this.itemManager = new ExposedItemManager();
        this.positionManager = new PositionManager();
    }

    /**
     * Resets the <code>PlayerField</code> to its initial state.
     * <p>
     * This method calls the reset method on both the <code>ExposedItemManager</code> and the
     * <code>PositionManager</code>. The itemManager's reset method clears all exposed items on the
     * PlayerField. The positionManager's reset method clears all positioned cards on the
     * PlayerField.
     */
    public void clearAll() {
        this.itemManager.reset();
        this.positionManager.reset();
    }

    /**
     * Places a <code>StarterCard</code> on the <code>PlayerField</code> at the starter position
     * (0,0).
     * <p>
     * This method first positions the card. Then, it updates the exposed items. The method does not
     * return any points as the placement of a <code>StarterCard</code> does not yield any points.
     *
     * @param firstCard The <code>StarterCard</code> to be placed on the <code>PlayerField</code>.
     * @param isRetro   A boolean indicating whether the card is placed in retro mode.
     * @throws IllegalCardPlacingException If an attempt is made to place a <code>StarterCard</code>
     *                                     more than once.
     */
    public void placeStartingCard(@NotNull StarterCard firstCard,
                                  boolean isRetro)
    throws IllegalCardPlacingException {
        Position starterPos = Position.of(0, 0);

        // Position the card
        this.positionManager.placeCard(firstCard, starterPos, isRetro);

        // Update exposed items
        this.itemManager.addExposedItemOn(firstCard, isRetro);
    }

    /**
     * Places a <code>PlayableCard</code> on the <code>PlayerField</code> at a specified Position.
     * <p>
     * The starter position is defined as (0, 0).
     * <p>
     * If the provided position matches the starter position, an
     * <code>IllegalCardPlacingException</code> is thrown, indicating that a
     * <code>PlayableCard</code> cannot be placed in the starter position.
     * <p>
     * Then, it positions the card and updates the exposed items. Finally, it returns the points
     * gained from placing the card.
     *
     * @param card     The <code>PlayableCard</code> to be placed on the <code>PlayerField</code>.
     * @param position The <code>Position</code> at which the card is to be placed.
     * @param isRetro  A boolean indicating whether the card is placed in retro mode.
     * @return The points gained from placing the card.
     * @throws IllegalCardPlacingException If an attempt is made to place a
     *                                     <code>PlayableCard</code> in the starter position or the
     *                                     position in unavailable.
     */
    public int place(@NotNull PlayableCard card,
                     @NotNull Position position,
                     boolean isRetro)
    throws IllegalCardPlacingException {

        // Check if position is a starter position
        if (Objects.equals(position, Position.of(0, 0)))
            throw new IllegalCardPlacingException("Cannot place PlayableCard in starter position!");

        // Position and update exposed items
        this.positionManager.placeCard(card, position, isRetro)
                            .forEach(this.itemManager::subToExposed);
        this.itemManager.addCardColor(card.getColor());
        this.itemManager.addExposedItemOn(card, isRetro);

        // returns the points gained from placing the card
        if (isRetro)
            return 0;
        return card.countPoints(this, position);
    }

    /**
     * It is used to get a set of all available positions on the player's field.
     * <p>
     * The availability of a position is determined by the PositionManager.
     * <p>
     * A position is considered available if it is not occupied by a card, and it is adjacent to a
     * card already placed on the field.
     *
     * @return A set of all available positions on the player's field.
     */
    public Set<Position> getAvailablePositions() {
        return this.positionManager.getAvailablePositions();
    }

    /**
     * It is used to check if a specific position on the player's field is available.
     * <p>
     * The availability of a position is determined by the PositionManager.
     * <p>
     * A position is considered available if it is not occupied by a card, and it is adjacent to a
     * card already placed on the field.
     *
     * @param position Position - The position to check for availability.
     * @return boolean - True if the position is available, false otherwise.
     */
    public boolean isAvailable(@NotNull Position position) {
        return this.positionManager.isAvailable(position);
    }

    /**
     * It is used to get the number of a specific item exposed on the player's field.
     * <p>
     * The item is specified by the parameter passed to the method.
     * <p>
     * The number of specific items is determined by the ExposedItemManager.
     *
     * @param item Item - The item for which to get the count.
     * @return int - The number of the specified item exposed on the player's field.
     */
    public int getNumberOf(@NotNull Item item) {
        return itemManager.getExposedItem(item);
    }

    /**
     * It is used to check if a specific card is present on the player's field.
     * <p>
     * The presence of a card is determined by the PositionManager.
     *
     * @param card FieldCard - The card to check for presence on the field.
     * @return boolean - True if the card is present on the field, false otherwise.
     */
    public boolean containsCard(@NotNull FieldCard card) {
        return this.positionManager.containsCard(card);
    }

    /**
     * It is used to get the map of cards positioned on the player's field.
     *
     * @return Map - A map of cards positioned on the player's field.
     */
    public Map<Position, CardContainer> getCardsPositioned() {
        return this.positionManager.getCardsPositioned();
    }

    /**
     * It is used to get the number of "placed" colors in the player's field.
     *
     * @return Map - A map of colors and the number of exposed items of that color.
     */
    public Map<GameColor, Integer> getPlacedCardColours() {
        return this.itemManager.getPlacedCardColors();
    }

    /**
     * It is used to get the number of items of a specific color exposed on the player's field.
     *
     * @param color GameColor - The color for which to get the count.
     * @return int - The number of items of the specified color exposed on the player's field.
     */
    public int getNumberOfPositionedColor(@NotNull GameColor color) {
        return this.itemManager.getPlacedCardOf(color);
    }

    /**
     * It is used to check if the requirements for placing a specific card on the player's field are
     * met.
     * <p>
     * The requirements are determined by the ExposedItemManager.
     * <p>
     * For a StarterCard, the method always returns true as there are no requirements for placing a
     * StarterCard.
     * <p>
     * For a PlayableCard, the method checks the requirements of the card.
     *
     * @param card    FieldCard - The card to check for requirement satisfaction.
     * @param isRetro boolean - A flag indicating whether the card is placed in retro mode.
     * @return boolean - True if the requirements for placing the card are met, false otherwise.
     */
    public boolean isRequirementMet(@NotNull FieldCard card, boolean isRetro) {
        return switch (card) {
            case StarterCard ignored -> true;
            case PlayableCard playableCard ->
                    this.itemManager.isRequirementsMet(playableCard, isRetro);
        };
    }

    /**
     * It is used to save the current state of the player's field.
     * <p>
     * The state of the player's field includes the state of the ExposedItemManager and the
     * PositionManager.
     *
     * @return FieldMemento - A FieldMemento object representing the current state of the player's
     * field.
     */
    public @NotNull FieldMemento save() {
        return new FieldMemento(itemManager.save(), positionManager.save());
    }

    /**
     * It is used to load a saved state of the player's field.
     * <p>
     * The state of the player's field includes the state of the ExposedItemManager and the
     * PositionManager.
     *
     * @param memento FieldMemento - The state to load into the player's field.
     */
    public void load(@NotNull FieldMemento memento) {
        this.itemManager.load(memento.exposedItems());
        this.positionManager.load(memento.positionManager());
    }

}
