package it.polimi.ingsw.am11.view.client.miniModel;

import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.players.field.PositionManager;
import it.polimi.ingsw.am11.model.players.utils.Position;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CliField {
    private final @NotNull Map<Position, MiniCardContainer> cardsPositioned;

    public CliField() {
        cardsPositioned = new HashMap<>(20);
    }

    public void absolutePosition(Position pos, int cardId, boolean isRetro, Map<Corner,
            Boolean> coveredCorners) {
        MiniCardContainer cardContainer = new MiniCardContainer(cardId, isRetro, coveredCorners);
        cardsPositioned.put(pos, cardContainer);
    }

    public void place(Position pos, int cardId, boolean isRetro) {

        MiniCardContainer cardContainer = new MiniCardContainer(cardId, isRetro);
        Stream.of(Corner.values())
              .map(corner -> PositionManager.getPositionIn(pos, corner))
              .filter(cardsPositioned::containsKey)
              .forEach(position -> {
                  MiniCardContainer neighbour = cardsPositioned.get(position);
                  Corner cornerToCover = PositionManager.getCornerFromPositions(position, pos)
                                                        .orElseThrow();
                  neighbour.cover(cornerToCover);
              });

        cardsPositioned.put(pos, cardContainer);
    }

    public void remove(Position pos) {
        cardsPositioned.remove(pos);
    }

    public Map<Position, MiniCardContainer> getCardsPositioned() {
        return Map.copyOf(cardsPositioned);
    }

    @Override
    public String toString() {
        StringBuilder result;
        result = new StringBuilder();
        for (MiniCardContainer miniCardContainer : cardsPositioned.values()) {
            result.append("\n").append(miniCardContainer.toString());
        }
        return result.toString();
    }

}
