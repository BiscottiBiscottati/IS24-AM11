package it.polimi.ingsw.am11.players;

import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.cards.utils.FieldCard;
import it.polimi.ingsw.am11.cards.utils.Item;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.cards.utils.helpers.EnumMapUtils;
import it.polimi.ingsw.am11.exceptions.IllegalPositioningException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PlayerField {

    private final EnumMap<Color, Integer> exposedColors;
    private final EnumMap<Symbol, Integer> exposedSymbols;
    private final EnumMap<Color, Integer> placedCardColors;
    private final Set<Position> availablePositions;
    private final Set<Position> closedPositions;
    private final Map<Position, CardContainer> cardsPositioned;

    public PlayerField() {
        this.exposedColors = EnumMapUtils.Init(Color.class, 0);
        this.exposedSymbols = EnumMapUtils.Init(Symbol.class, 0);
        this.placedCardColors = EnumMapUtils.Init(Color.class, 0);
        this.availablePositions = new HashSet<>(16);
        this.availablePositions.add(Position.of(0, 0));
        this.closedPositions = new HashSet<>(32);
        this.cardsPositioned = new HashMap<>(40);
    }

    /**
     * Returns a new {@link Position Position} object
     * that represents the position in a specified corner relative to a given position.
     * <p>
     * This method is static and can be called without creating an instance of the <code>PlayerField</code> class.
     * It takes a <code>Position</code> object and a <code>Corner</code> enum as parameters
     * and calculates the new position based on the specified corner.
     * <p>
     * The method uses a switch statement to determine the new position based on the specified corner.
     * If the specified corner is not a valid <code>Corner</code> enum,
     * the method throws an <code>EnumConstantNotPresentException</code>.
     *
     * @param position The original position.
     * @param corner   The corner relative to the original position.
     * @return A new <code>Position</code> object that represents the position in
     * the specified corner relative to the original position.
     * @throws EnumConstantNotPresentException if the specified corner is not a valid Corner enum.
     */
    @Contract("_, _ -> new")
    public static @NotNull Position getPositionIn(@NotNull Position position, @NotNull Corner corner) {
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
            default -> throw new EnumConstantNotPresentException(Corner.class, "Qualcosa non va con lo switch enum!");

        }
    }

    /**
     * Returns a new {@link Position Position} object
     * that represents the final position after moving from a given position through a list of corners.
     * <p>
     * This method is static and can be called without creating an instance of the <code>PlayerField</code> class.
     * It takes a <code>Position</code> object and a <code>List</code> of <code>Corner</code> enums as parameters
     * and calculates the new position based on the specified corners.
     * <p>
     * The final position is the result of moving from the original position through all the corners in the list.
     * <p>
     * Note: The list is processed in order,
     * so the final position is the result of moving through the corners in the order they appear in the list.
     *
     * @param currentPosition The original position.
     * @param corners         The list of corners to move through.
     * @return A new Position object that represents the final position after moving through all the corners.
     */
    @Contract("_, _ -> new")
    public static Position getMovementOfPositions(@NotNull Position currentPosition, @NotNull List<Corner> corners) {
        return corners.stream().reduce(currentPosition, PlayerField::getPositionIn, (a, b) -> b);
    }

    public static Optional<Corner> getCornerFromPositions(@NotNull Position firstPos,
                                                          @NotNull Position secondPos) {
        int deltaX = secondPos.x() - firstPos.x();
        int deltaY = secondPos.y() - firstPos.y();
        if (deltaX == 1 && deltaY == 1) return Optional.of(Corner.TOP_RX);
        if (deltaX == -1 && deltaY == 1) return Optional.of(Corner.TOP_LX);
        if (deltaX == 1 && deltaY == -1) return Optional.of(Corner.DOWN_RX);
        if (deltaX == -1 && deltaY == -1) return Optional.of(Corner.DOWN_LX);
        return Optional.empty();
    }

    public Map<Position, CardContainer> getCardsPositioned() {
        return cardsPositioned;
    }

    public EnumMap<Color, Integer> getExposedColours() {
        return exposedColors;
    }

    public EnumMap<Symbol, Integer> getExposedSymbols() {
        return exposedSymbols;
    }

    public EnumMap<Color, Integer> getPlacedCardColours() {
        return placedCardColors;
    }

    public int getNumberOf(@NotNull Item item) {
        switch (item) {
            case Color color -> {
                return this.exposedColors.get(color);
            }
            case Symbol symbol -> {
                return this.exposedSymbols.get(symbol);
            }
            default -> throw new IllegalArgumentException("Item not recognized!");
        }
    }

    private void addNumberOf(@NotNull CornerContainer cornerContainer, int number) {
        switch (cornerContainer) {
            case Color color -> this.exposedColors.put(color, number);
            case Symbol symbol -> this.exposedSymbols.put(symbol, number);
            default -> {
            }
        }
    }

    private void updatePositions(FieldCard card, boolean isRetro) {
        Arrays.stream(Corner.values())
              .filter(corner -> !card.isAvailable(corner, isRetro))
              .map(corner -> PlayerField.getPositionIn(Position.of(0, 0), corner))
              .forEach(this.closedPositions::add);
        Arrays.stream(Corner.values())
              .filter(corner -> card.isAvailable(corner, isRetro))
              .map(corner -> PlayerField.getPositionIn(Position.of(0, 0), corner))
              .forEach(this.availablePositions::add);
        this.availablePositions.removeAll(this.closedPositions);
    }

    // TODO needs to do the logic to add placed card colors and maps CardContainers
    public void placeStartingCard(StarterCard firstCard, boolean isRetro) throws IllegalPositioningException {
        if (this.cardsPositioned.getOrDefault(Position.of(0, 0), null) == null) {
            this.cardsPositioned.put(Position.of(0, 0), new CardContainer(firstCard, isRetro));
            this.availablePositions.remove(Position.of(0, 0));

            Arrays.stream(Corner.values())
                  .map(corner -> firstCard.checkItemCorner(corner, isRetro))
                  .forEach(cornerContainer -> addNumberOf(cornerContainer, 1));

            updatePositions(firstCard, isRetro);


        } else throw new IllegalPositioningException("Cannot place another starter!");
    }


    // TODO needs to do the logic to add placed card colors and update maps CardContainers
    public void place(PlayableCard card, Position position, boolean isRetro) throws IllegalPositioningException {
        if (this.availablePositions.contains(position)) {
            CardContainer temp = new CardContainer(card, isRetro);
            this.cardsPositioned.put(position, temp);
            this.availablePositions.remove(position);


            Arrays.stream(Corner.values())
                  .map(corner -> card.checkItemCorner(corner, isRetro))
                  .forEach(cornerContainer -> addNumberOf(cornerContainer, 1));


            updatePositions(card, isRetro);
        } else throw new IllegalPositioningException("Cannot place card in that position!");
    }

    public Set<Position> getAvailablePositions() {
        return availablePositions;
    }

    public boolean isPositionAvailable(Position position) {
        return availablePositions.contains(position);
    }
}
