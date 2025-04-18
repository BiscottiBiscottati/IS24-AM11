package it.polimi.ingsw.am11.model.cards.utils.enums;

import com.google.common.collect.ImmutableSet;
import it.polimi.ingsw.am11.model.players.utils.Position;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type of objective card.
 * <p>
 * Can be <code>COLOR_COLLECT</code>, <code>L_SHAPE</code>, <code>SYMBOL_COLLECT</code> or
 * <code>TRIPLET</code>.
 */
public enum ObjectiveCardType {

    COLOR_COLLECT(null),
    L_SHAPE(Set.of(
            Position.of(1, 1),
            Position.of(1, 3),
            Position.of(2, 0)
    )),
    SYMBOL_COLLECT(null),
    TRIPLET(Set.of(
            Position.of(0, 2),
            Position.of(1, 1),
            Position.of(2, 0)
    )
    );

    private static final int MAX_LENGTH_X = 3;
    private static final int MAX_LENGTH_Y = 4;
    private final Set<Position> positions;

    ObjectiveCardType(Set<Position> positions) {
        this.positions = positions;
    }

    /**
     * Specifies a set of positions x and y where given a 3x3 matrix create the pattern of the
     * card.
     * <p>
     * If the objective card way to score points is not pattern matching, then returns an empty
     * <code>Optional</code>
     *
     * @param flipped if the pattern is flipped on its y-axis
     * @param rotated if the pattern is rotated by 180 degrees
     * @return an <code>Optional</code> of a set of positions
     */
    public @NotNull Optional<Set<Position>> getPositions(boolean flipped, boolean rotated) {
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

    /**
     * Flips a set of positions in a 3x3 matrix on its x-axis
     *
     * @param positions The positions to flip
     * @return A new set of positions flipped
     */
    private static @NotNull Set<Position> flipX(@NotNull Set<Position> positions) {
        return positions.stream()
                        .map(position -> Position.of(MAX_LENGTH_X - position.x() - 1, position.y()))
                        .collect(Collectors.toSet());
    }

    /**
     * Flips a set of positions in a 3x3 matrix on its y-axis
     *
     * @param positions The positions to flip
     * @return A new set of position flipped
     */
    private static @NotNull Set<Position> flipY(@NotNull Set<Position> positions) {
        return positions.stream()
                        .map(position -> Position.of(position.x(), MAX_LENGTH_Y - position.y() - 1))
                        .collect(Collectors.toSet());
    }
}
