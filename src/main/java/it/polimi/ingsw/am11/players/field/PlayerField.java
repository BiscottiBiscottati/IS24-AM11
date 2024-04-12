package it.polimi.ingsw.am11.players.field;

import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.utils.FieldCard;
import it.polimi.ingsw.am11.cards.utils.Item;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.exceptions.IllegalCardPlacingException;
import it.polimi.ingsw.am11.players.CardContainer;
import it.polimi.ingsw.am11.players.Position;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PlayerField {

    private final ExposedItemManager itemManager;
    private final PositionManager positionManager;

    public PlayerField() {
        this.itemManager = new ExposedItemManager();
        this.positionManager = new PositionManager();
    }

    /**
     * Resets the <code>PlayerField</code> to its initial state.
     * <p>
     * This method calls the reset method on both the <code>ExposedItemManager</code>
     * and the <code>PositionManager</code>.
     * The itemManager's reset method clears all exposed items on the PlayerField.
     * The positionManager's reset method clears all positioned cards on the PlayerField.
     */
    public void clearAll() {
        this.itemManager.reset();
        this.positionManager.reset();
    }

    /**
     * Places a <code>StarterCard</code> on the <code>PlayerField</code> at the starter position (0,0).
     * <p>
     * This method first positions the card.
     * Then, it updates the exposed items.
     * The method does not return any points as the placement of a <code>StarterCard</code> does not yield any points.
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
     * If the provided position matches the starter position,
     * an <code>IllegalCardPlacingException</code> is thrown,
     * indicating that a <code>PlayableCard</code> cannot be placed in the starter position.
     * <p>
     * Then, it positions the card and updates the exposed items.
     * Finally, it returns the points gained from placing the card.
     *
     * @param card     The <code>PlayableCard</code> to be placed on the <code>PlayerField</code>.
     * @param position The <code>Position</code> at which the card is to be placed.
     * @param isRetro  A boolean indicating whether the card is placed in retro mode.
     * @return The points gained from placing the card.
     * @throws IllegalCardPlacingException If an attempt is made to place a <code>PlayableCard</code>
     *                                     in the starter position or the position in unavailable.
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
        return card.countPoints(this, position);
    }

    public Set<Position> getAvailablePositions() {
        return this.positionManager.getAvailablePositions();
    }

    public boolean isAvailable(@NotNull Position position) {
        return this.positionManager.isAvailable(position);
    }

    public int getNumberOf(@NotNull Item item) {
        return itemManager.getExposedItem(item);
    }

    public boolean containsCard(@NotNull FieldCard card) {
        return this.positionManager.containsCard(card);
    }

    public Map<Position, CardContainer> getCardsPositioned() {
        return this.positionManager.getCardsPositioned();
    }

    public Map<Color, Integer> getPlacedCardColours() {
        return this.itemManager.getPlacedCardColors();
    }

    public int getNumberOfPositionedColor(@NotNull Color color) {
        return this.itemManager.getPlacedCardOf(color);
    }

    public boolean isRequirementMet(@NotNull FieldCard card, boolean isRetro) {
        return switch (card) {
            case StarterCard ignored -> true;
            case PlayableCard playableCard -> this.itemManager.isRequirementsMet(playableCard, isRetro);
        };
    }

}
