package it.polimi.ingsw.am11.cards.utils;

import com.google.common.collect.ImmutableSet;
import it.polimi.ingsw.am11.players.Position;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public enum ObjectiveCardType {
    COLOR_COLLECT(null),
    L_SHAPE(Set.of(
            Position.of(1, 1),
            Position.of(1, 2),
            Position.of(2, 0)
    )),
    SYMBOL_COLLECT(null),
    TRIPLET(Set.of(
            Position.of(0, 2),
            Position.of(1, 1),
            Position.of(2, 0)
    )
    );

    private static final int MAX_LENGTH = 3;
    private final Set<Position> positions;

    ObjectiveCardType(Set<Position> positions) {
        this.positions = positions;
    }

    private static Set<Position> flipY(@NotNull Set<Position> positions) {
        return positions.stream()
                        .map(position -> Position.of(position.x(), MAX_LENGTH - position.y() - 1))
                        .collect(Collectors.toSet());
    }

    private static Set<Position> flipX(@NotNull Set<Position> positions) {
        return positions.stream()
                        .map(position -> Position.of(MAX_LENGTH - position.x() - 1, position.y()))
                        .collect(Collectors.toSet());
    }

    public Optional<Set<Position>> getPositions(boolean flipped, boolean rotated) {
        Set<Position> result = this.positions;
        if (result != null) {
            if (flipped) {
                result = flipX(result);
            }
            if (rotated) {
                result = flipX(result);
                result = flipY(result);
            }
            return Optional.of(ImmutableSet.copyOf(result));
        }
        return Optional.empty();
    }
}
