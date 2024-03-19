package it.polimi.ingsw.am11.cards.utils;

import com.google.common.collect.ImmutableList;
import it.polimi.ingsw.am11.players.Position;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public enum ObjectiveCardType {
    COLOR_COLLECT(null),
    L_SHAPE(List.of(
            Position.of(0, 1),
            Position.of(1, 1),
            Position.of(2, 2)
    )),
    OBJECT_COLLECT(null),
    TRIPLET(List.of(
            Position.of(0, 2),
            Position.of(1, 1),
            Position.of(2, 0)
    )
    );

    private static final int MAX_LENGTH = 3;
    private final List<Position> positions;

    ObjectiveCardType(List<Position> positions) {
        this.positions = positions;
    }

    private static List<Position> flipY(@NotNull List<Position> positions) {
        return positions.stream()
                        .map(position -> Position.of(position.x(), MAX_LENGTH - position.y() - 1))
                        .toList();
    }

    private static List<Position> flipX(@NotNull List<Position> positions) {
        return positions.stream()
                        .map(position -> Position.of(MAX_LENGTH - position.x() - 1, position.y()))
                        .sorted()
                        .toList();
    }

    public Optional<List<Position>> getPositions(boolean flipped, boolean rotated) {
        List<Position> result = this.positions;
        if (result != null) {
            if (flipped) {
                result = flipY(result);
            }
            if (rotated) {
                result = flipX(result);
                result = flipY(result);
            }
            return Optional.of(ImmutableList.copyOf(result));
        }
        return Optional.empty();
    }
}
