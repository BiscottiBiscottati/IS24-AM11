package it.polimi.ingsw.am11.cards.utils.enums;

import com.google.common.collect.ImmutableSet;
import it.polimi.ingsw.am11.players.Position;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type of objective card.
 * <p>
 * Can be of collecting symbols or colors or a pattern of card.
 */
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

    /**
     * Flips a set of positions in a 3x3 matrix on its y-axis
     *
     * @param positions The positions to flip
     * @return A new set of position flipped
     */
    private static Set<Position> flipY(@NotNull Set<Position> positions) {
        return positions.stream()
                        .map(position -> Position.of(position.x(), MAX_LENGTH - position.y() - 1))
                        .collect(Collectors.toSet());
    }

    /**
     * Flips a set of positions in a 3x3 matrix on its x-axis
     *
     * @param positions The positions to flip
     * @return A new set of positions flipped
     */
    private static Set<Position> flipX(@NotNull Set<Position> positions) {
        return positions.stream()
                        .map(position -> Position.of(MAX_LENGTH - position.x() - 1, position.y()))
                        .collect(Collectors.toSet());
    }

    /**
     * Specifies a set of positions x and y where given a 3x3 matrix create the pattern of the card.
     * <p>
     * If the objective card way to score points is not pattern matching,
     * then returns an empty <code>Optional</code>
     *
     * @param flipped if the pattern is flipped on its y-axis
     * @param rotated if the pattern is rotated by 180 degrees
     * @return an <code>Optional</code> of a set of positions
     */
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
