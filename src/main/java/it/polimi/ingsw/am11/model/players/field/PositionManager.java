package it.polimi.ingsw.am11.model.players.field;

import it.polimi.ingsw.am11.model.cards.utils.FieldCard;
import it.polimi.ingsw.am11.model.cards.utils.Item;
import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardPlacingException;
import it.polimi.ingsw.am11.model.players.utils.CardContainer;
import it.polimi.ingsw.am11.model.players.utils.Position;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

/**
 * Manages the positioning of cards on the field in the game.
 * <p>
 * This class is responsible for keeping track of the positions of cards on the field, determining
 * which positions are available for placing new cards, and handling the placement of new cards on
 * the field.
 * <p>
 * It maintains a set of available positions where new cards can be placed, a set of closed
 * positions where no new cards can be placed, and a map that associates each positioned card with
 * its position on the field.
 * <p>
 * It also keeps track of the last card that was placed on the field, which can be useful for
 * undoing the last move or for other game mechanics that depend on the order of card placement.
 */
public class PositionManager {
    private final @NotNull Set<Position> availablePositions;
    private final @NotNull Set<Position> closedPositions;
    private final @NotNull Map<Position, CardContainer> cardsPositioned;
    private FieldCard lastPlacedCard;

    /**
     * Constructor for the <code>PositionManager</code> class.
     * <p>
     * This constructor initializes the availablePositions set with a single position at (0,0), the
     * closedPositions set as an empty set, and the cardsPositioned map as an empty map. It also
     * sets the lastPlacedCard to null.
     * <p>
     * The initial capacity for the availablePositions and closedPositions sets is 32, and for the
     * cardsPositioned map is 64. These values are chosen as a performance optimization to reduce
     * the need for resizing the collections as cards are placed on the field.
     */
    PositionManager() {
        this.availablePositions = new HashSet<>(32);
        this.availablePositions.add(Position.of(0, 0));
        this.closedPositions = new HashSet<>(32);
        this.cardsPositioned = new HashMap<>(64);
        this.lastPlacedCard = null;
    }

    /**
     * Calculates the final position after a series of movements from a given position.
     * <p>
     * This method takes a {@link Position} object representing the current position and a
     * <code>List</code> of {@link Corner} objects
     * representing the directions of the movements. It applies each movement in the list to the
     * current position in order, and returns the final position after all movements have been
     * applied.
     * <p>
     * If the list of movements is empty, this method returns the current position unchanged.
     *
     * @param currentPosition The current position.
     * @param corners         The list of movements to apply.
     * @return The final position after applying all movements.
     */
    public static Position getMovementOfPositions(@NotNull Position currentPosition,
                                                  @NotNull List<Corner> corners) {
        return corners.stream().reduce(currentPosition, PositionManager::getPositionIn,
                                       (a, b) -> b);
    }

    /**
     * Calculates the new position based on the current position and the direction of movement.
     * <p>
     * This method takes a {@link Position} object representing the current position and a
     * {@link Corner} object representing the direction of the movement. It calculates the new
     * position after applying the movement to the current position and returns the new position.
     * <p>
     * The movement is applied by changing the x and y coordinates of the current position based on
     * the specified direction. For example, if the direction is <code>TOP_RX</code>, the x and y
     * coordinates are both increased by 1.
     *
     * @param position The current position.
     * @param corner   The direction of the movement.
     * @return The new position after applying the movement.
     */
    @Contract("_, _ -> new")
    public static @NotNull Position getPositionIn(@NotNull Position position,
                                                  @NotNull Corner corner) {
        int tempX = position.x();
        int tempY = position.y();
        switch (corner) {
            case TOP_RX -> {
                return new Position(tempX + 1, tempY + 1);
            }
            case TOP_LX -> {
                return new Position(tempX - 1, tempY + 1);
            }
            case DOWN_RX -> {
                return new Position(tempX + 1, tempY - 1);
            }
            case DOWN_LX -> {
                return new Position(tempX - 1, tempY - 1);
            }
            default -> throw new EnumConstantNotPresentException(Corner.class,
                                                                 "Qualcosa non va con lo switch " +
                                                                 "enum!");
        }
    }

    /**
     * Resets the state of the PositionManager.
     * <p>
     * This method clears all the positions stored in the availablePositions, closedPositions, and
     * cardsPositioned collections. It then adds a single position at (0,0) to the
     * availablePositions set, effectively resetting the state of the PositionManager to its initial
     * state.
     */
    void reset() {
        this.availablePositions.clear();
        this.availablePositions.add(Position.of(0, 0));
        this.closedPositions.clear();
        this.cardsPositioned.clear();
    }

    /**
     * Places a card on the field at a specified position.
     * <p>
     * This method takes a {@link FieldCard} object representing the card to be placed, a
     * {@link Position} object representing the position where the card should be placed, and a
     * boolean indicating whether the card is placed in retro mode.
     * <p>
     * It first checks if the specified position is available for placing the card. If the position
     * is available, it removes the position from the availablePositions set and creates a new
     * {@link CardContainer} object of the <code>FieldCard</code> to the cardsPositioned map,
     * associating the card with the specified position.
     * <p>
     * It then updates the availablePositions and closedPositions sets based on the availability of
     * the corners of the new card. Positions adjacent to an available corner of the new card are
     * added to the availablePositions set, and positions adjacent to a closed corner of the new
     * card are added to the closedPositions set.
     * <p>
     * Finally, it sets the lastPlacedCard to the new card and returns a list of {@link Item}
     * objects representing the items that are covered by the new card.
     * <p>
     * If the specified position is not available for placing the card, this method throws an
     * {@link IllegalCardPlacingException}.
     *
     * @param card     The card to be placed.
     * @param position The position where the card should be placed.
     * @param isRetro  Indicates whether the card is placed in retro mode.
     * @return A list of items that are covered by the new card.
     * @throws IllegalCardPlacingException If the specified position is not available for placing
     *                                     the card.
     */
    @NotNull
    List<Item> placeCard(@NotNull FieldCard card,
                         @NotNull Position position,
                         boolean isRetro)
    throws IllegalCardPlacingException {

        // Check if the position is available and position the card
        if (availablePositions.contains(position)) {
            availablePositions.remove(position);
            cardsPositioned.put(position, new CardContainer(card, isRetro));
        } else {
            throw new IllegalCardPlacingException("Cannot position: " + card
                                                  + " in position: " + position
                                                  + " because it is not available.");
        }

        // Update available and closed positions
        Arrays.stream(Corner.values())
              .filter(corner -> ! card.isAvailable(corner, isRetro))
              .map(corner -> PositionManager.getPositionIn(position, corner))
              .forEach(this.closedPositions::add);
        Arrays.stream(Corner.values())
              .filter(corner -> card.isAvailable(corner, isRetro))
              .map(corner -> PositionManager.getPositionIn(position, corner))
              .filter(availablePos -> ! this.cardsPositioned.containsKey(availablePos))
              .forEach(this.availablePositions::add);
        this.availablePositions.removeAll(this.closedPositions);

        this.lastPlacedCard = card;

        // Update the cards that are covered by the new card and return the List of Items being
        // covered
        return Stream.of(Corner.values())
                     .map(corner -> PositionManager.getPositionIn(position, corner))
                     .filter(this.cardsPositioned::containsKey)
                     .map(pos -> {
                         Corner cornerToCover = PositionManager.getCornerFromPositions(
                                 pos,
                                 position).orElseThrow();
                         return this.cardsPositioned.get(pos).cover(cornerToCover);
                     })
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .toList();
    }

    /**
     * Determines the corner from the positions of two adjacent cells.
     * <p>
     * This method takes two {@link Position} objects representing the positions of two adjacent
     * cells on the field. It calculates the difference in the x and y coordinates of the two
     * positions to determine the direction from the first position to the second position. It then
     * returns an <code>Optional</code> containing the {@link Corner} corresponding to this
     * direction.
     * <p>
     * If the two positions are not adjacent or if they are diagonally adjacent, this method returns
     * an empty <code>Optional</code>.
     *
     * @param posToCheck The position of the first cell.
     * @param posTarget  The position of the second cell.
     * @return An <code>Optional</code> containing the <code>Corner</code> corresponding to the
     * direction from the first position to the second position, or an empty <code>Optional</code>
     * if the two positions are not adjacent or if they are diagonally adjacent.
     */
    public static @NotNull Optional<Corner> getCornerFromPositions(@NotNull Position posToCheck,
                                                                   @NotNull Position posTarget) {
        int deltaX = posTarget.x() - posToCheck.x();
        int deltaY = posTarget.y() - posToCheck.y();
        if (deltaX == 1 && deltaY == 1) return Optional.of(Corner.TOP_RX);
        if (deltaX == - 1 && deltaY == 1) return Optional.of(Corner.TOP_LX);
        if (deltaX == 1 && deltaY == - 1) return Optional.of(Corner.DOWN_RX);
        if (deltaX == - 1 && deltaY == - 1) return Optional.of(Corner.DOWN_LX);
        return Optional.empty();
    }

    /**
     * Retrieves a copy of the set of available positions.
     * <p>
     * This method returns a copy of the availablePositions set, which represents all the positions
     * on the field where a new card can be placed. The returned set is a copy, so changes to the
     * returned set will not affect the availablePositions set in the PositionManager.
     *
     * @return A copy of the set of available positions.
     */
    public Set<Position> getAvailablePositions() {
        return Set.copyOf(availablePositions);
    }

    /**
     * Checks if a given position is available for placing a card.
     * <p>
     * This method takes a {@link Position} object representing the position to check. It returns a
     * boolean indicating whether the position is available for placing a card. A position is
     * considered available if it is not occupied by a card and if it is not adjacent to a closed
     * corner of a card.
     *
     * @param position The position to check.
     * @return True if the position is available for placing a card, false otherwise.
     */
    public boolean isAvailable(@NotNull Position position) {
        return availablePositions.contains(position);
    }

    public @NotNull Optional<CardContainer> getCardIfExists(@NotNull Position position) {
        return Optional.ofNullable(cardsPositioned.get(position));
    }

    /**
     * Checks if a given card is already placed on the field.
     * <p>
     * This method takes a {@link FieldCard} object representing the card to check. It returns a
     * boolean indicating whether the card is already placed on the field. The check is performed by
     * comparing the given card with each card in the cardsPositioned map.
     *
     * @param card The card to check.
     * @return True if the card is already placed on the field, false otherwise.
     */
    public boolean containsCard(@NotNull FieldCard card) {
        return cardsPositioned.values()
                              .stream()
                              .anyMatch(cardContainer -> cardContainer.isCardEquals(card));
    }

    public FieldCard resetLastMove() {
        this.cardsPositioned.entrySet()
                            .stream()
                            .filter(entry -> entry.getValue().getCard().equals(lastPlacedCard))
                            .map(Map.Entry::getKey)
                            .forEach(cardsPositioned::remove);

        this.availablePositions.clear();
        this.closedPositions.clear();

        this.cardsPositioned.keySet()
                            .stream()
                            .flatMap(pos -> Stream.of(Corner.values())
                                                  .map(corner -> PositionManager.getPositionIn(
                                                          pos, corner)))
                            .distinct()
                            .filter(pos -> ! this.cardsPositioned.containsKey(pos))
                            .forEach(position -> {
                                if (isAdjacentClosed(position)) {
                                    closedPositions.add(position);
                                } else {
                                    availablePositions.add(position);
                                }
                            });
        // TODO may need to test

        return lastPlacedCard;
    }

    private boolean isAdjacentClosed(@NotNull Position position) {
        return Stream.of(Corner.values())
                     .map(corner -> PositionManager.getPositionIn(position, corner))
                     .anyMatch(adjPos ->
                                       Optional.ofNullable(this.cardsPositioned.get(adjPos))
                                               .map(cardContainer -> cardContainer.isCornerCovered(
                                                       PositionManager.getCornerFromPositions(
                                                               adjPos,
                                                               position).orElseThrow()))
                                               .orElse(false));
    }

    /**
     * Retrieves a copy of the map of positioned cards.
     * <p>
     * This method returns a copy of the cardsPositioned map, which associates each positioned card
     * with its position on the field. The returned map is a copy, so changes to the returned map
     * will not affect the cardsPositioned map in the PositionManager.
     *
     * @return A copy of the map of positioned cards.
     */
    public Map<Position, CardContainer> getCardsPositioned() {
        return Map.copyOf(cardsPositioned);
    }
}
