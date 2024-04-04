package it.polimi.ingsw.am11.players;

import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
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
    private HashMap<Position, CardContainer> cardsPositioned;

    public PlayerField() {
        this.exposedColors = EnumMapUtils.Init(Color.class, 0);
        this.exposedSymbols = EnumMapUtils.Init(Symbol.class, 0);
        this.placedCardColors = EnumMapUtils.Init(Color.class, 0);
        this.availablePositions = new HashSet<>(16);

    }

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

    public static Position getMovementOfPositions(Position currentPosition, @NotNull List<Corner> corners) {
        return corners.stream().reduce(currentPosition, PlayerField::getPositionIn, (a, b) -> b);
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

    public int getNumberOf(Item item) {
        switch (item) {
            case Color color -> {
                return this.exposedColors.get(color);
            }
            case Symbol symbol -> {
                return this.exposedSymbols.get(symbol);
            }
            case null, default -> {
                return 0;
            }
        }
    }

    public void placeStartingCard(StarterCard firstCard) throws IllegalPositioningException {
        if (this.cardsPositioned.getOrDefault(Position.of(0, 0), null) == null) {
            this.cardsPositioned.put(Position.of(0, 0), new CardContainer(firstCard));
            availablePositions.addAll(
                    Arrays.stream(Corner.values())
                          .map(corner -> PlayerField.getPositionIn(Position.of(0, 0), corner))
                          .toList()
            );
        } else throw new IllegalPositioningException("Cannot place another starter!");
    }

    public void place(PlayableCard card, Position position) throws IllegalPositioningException {
        if (this.availablePositions.contains(position)) {
            this.cardsPositioned.put(position, new CardContainer(card));
            this.availablePositions.remove(position);
            this.availablePositions.addAll(
                    Arrays.stream(Corner.values())
                          .filter(card::isAvailable)
                          .map(corner -> PlayerField.getPositionIn(position, corner))
                          .toList());
        } else throw new IllegalPositioningException("Posizione non disponibile!");
    }

    public Set<Position> getAvailablePositions() {
        //TODO
        return null;
    }

    public boolean isPositionAvailable(Position position) {
        return this.availablePositions.contains(position);
    }
}
