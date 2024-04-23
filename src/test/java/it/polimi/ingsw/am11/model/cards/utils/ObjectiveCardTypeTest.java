package it.polimi.ingsw.am11.model.cards.utils;

import it.polimi.ingsw.am11.model.cards.utils.enums.ObjectiveCardType;
import it.polimi.ingsw.am11.model.players.utils.Position;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectiveCardTypeTest {

    static final Set<Position> standardTriplet = Set.of(
            Position.of(0, 2),
            Position.of(1, 1),
            Position.of(2, 0)
    );

    static final Set<Position> flippedTriplet = Set.of(
            Position.of(0, 0),
            Position.of(1, 1),
            Position.of(2, 2)
    );

    static final Set<Position> LShape = Set.of(
            Position.of(1, 3),
            Position.of(1, 1),
            Position.of(2, 0)
    );

    static final Set<Position> flippedL = Set.of(
            Position.of(1, 3),
            Position.of(1, 1),
            Position.of(0, 0)
    );

    static final Set<Position> rotatedL = Set.of(
            Position.of(1, - 1),
            Position.of(1, 1),
            Position.of(0, 2)
    );

    static final Set<Position> rotatedFlippedL = Set.of(
            Position.of(1, - 1),
            Position.of(1, 1),
            Position.of(2, 2)
    );


    @Test
    void getPositions() {

        assertEquals(
                standardTriplet,
                ObjectiveCardType.TRIPLET.getPositions(false, false).orElse(Set.of())
        );
        assertEquals(
                standardTriplet,
                ObjectiveCardType.TRIPLET.getPositions(false, true).orElse(Set.of())
        );
        assertEquals(
                flippedTriplet,
                ObjectiveCardType.TRIPLET.getPositions(true, false).orElse(Set.of())
        );
        assertEquals(
                flippedTriplet,
                ObjectiveCardType.TRIPLET.getPositions(true, true).orElse(Set.of())
        );
        assertEquals(
                LShape,
                ObjectiveCardType.L_SHAPE.getPositions(false, false).orElse(Set.of())
        );
        assertEquals(
                flippedL,
                ObjectiveCardType.L_SHAPE.getPositions(true, false).orElse(Set.of())
        );
        assertEquals(
                rotatedL,
                ObjectiveCardType.L_SHAPE.getPositions(false, true).orElse(Set.of())
        );
        assertEquals(
                rotatedFlippedL,
                ObjectiveCardType.L_SHAPE.getPositions(true, true).orElse(Set.of())
        );

    }
}