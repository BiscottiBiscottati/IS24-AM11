package it.polimi.ingsw.am11.cards.objective;

import it.polimi.ingsw.am11.cards.utils.Color;
import it.polimi.ingsw.am11.cards.utils.Corner;
import it.polimi.ingsw.am11.cards.utils.PatternCounter;
import it.polimi.ingsw.am11.cards.utils.PatternPurpose;
import it.polimi.ingsw.am11.players.CardContainer;
import it.polimi.ingsw.am11.players.PlayerField;
import it.polimi.ingsw.am11.players.Position;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class LPatternCounter implements PatternCounter {

    private final Color primaryColor;

    private final Color secondaryColor;

    private final EnumMap<PatternPurpose, List<Corner>> cornersPurpose;

    public LPatternCounter(Color primaryColor,
                           Color secondaryColor,
                           EnumMap<PatternPurpose, List<Corner>> cornersPurpose) {
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.cornersPurpose = cornersPurpose;
    }

    private static Position getMovementOfPositions(Position currentPosition, @NotNull List<Corner> corners) {
        BiFunction<Position, Corner, Position> translationBiFunction =
                ((position, corner) -> PlayerField.getPositionIn(corner, position));

        return corners.stream().reduce(currentPosition, translationBiFunction, (a, b) -> b);
    }

    private void countNumberOfPatterns(
            Map<Position, CardContainer> field,
            Position currentPosition,
            int numberSeen) {
        Position nextPatternPosition = getMovementOfPositions(
                currentPosition,
                this.cornersPurpose.get(PatternPurpose.NEXT_CHECK)
        );
        Position previousPatternPosition = getMovementOfPositions(
                currentPosition,
                this.cornersPurpose.get(PatternPurpose.PREVIOUS_CHECK)
        );
    }

    @Override
    public int count(PlayerField playerField) {
        return 0;
    }
}
