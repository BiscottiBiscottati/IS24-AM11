package it.polimi.ingsw.am11.cards.utils;

import it.polimi.ingsw.am11.players.Position;

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

    private final List<Position> positions;

    ObjectiveCardType(List<Position> positions) {
        this.positions = positions;
    }

    public Optional<List<Position>> getPositions() {
        return Optional.ofNullable(positions);
    }
}
