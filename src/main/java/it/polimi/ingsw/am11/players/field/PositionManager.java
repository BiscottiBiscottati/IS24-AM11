package it.polimi.ingsw.am11.players.field;

import it.polimi.ingsw.am11.cards.utils.FieldCard;
import it.polimi.ingsw.am11.cards.utils.Item;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.exceptions.IllegalCardPlacingException;
import it.polimi.ingsw.am11.players.utils.CardContainer;
import it.polimi.ingsw.am11.players.utils.Position;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public class PositionManager {
    private final @NotNull Set<Position> availablePositions;
    private final @NotNull Set<Position> closedPositions;
    private final @NotNull Map<Position, CardContainer> cardsPositioned;
    private FieldCard lastPlacedCard;

    PositionManager() {
        this.availablePositions = new HashSet<>(32);
        this.availablePositions.add(Position.of(0, 0));
        this.closedPositions = new HashSet<>(32);
        this.cardsPositioned = new HashMap<>(64);
        this.lastPlacedCard = null;
    }

    public static Position getMovementOfPositions(@NotNull Position currentPosition,
                                                  @NotNull List<Corner> corners) {
        return corners.stream().reduce(currentPosition, PositionManager::getPositionIn,
                                       (a, b) -> b);
    }

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

    void reset() {
        this.availablePositions.clear();
        this.availablePositions.add(Position.of(0, 0));
        this.closedPositions.clear();
        this.cardsPositioned.clear();
    }

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

    public Set<Position> getAvailablePositions() {
        return Set.copyOf(availablePositions);
    }

    public boolean isAvailable(@NotNull Position position) {
        return availablePositions.contains(position);
    }

    public @NotNull Optional<CardContainer> getCardIfExists(@NotNull Position position) {
        return Optional.ofNullable(cardsPositioned.get(position));
    }

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
                     .anyMatch(adjPos -> {
                         if (! this.cardsPositioned.containsKey(adjPos)) {
                             return false;
                         } else {
                             return this.cardsPositioned.get(adjPos)
                                                        .isCornerCovered(
                                                                PositionManager.getCornerFromPositions(
                                                                        adjPos,
                                                                        position).orElseThrow());
                         }
                     });
    }

    public Map<Position, CardContainer> getCardsPositioned() {
        return Map.copyOf(cardsPositioned);
    }
}
