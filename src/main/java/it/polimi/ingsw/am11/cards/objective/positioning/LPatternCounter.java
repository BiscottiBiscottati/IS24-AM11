package it.polimi.ingsw.am11.cards.objective.positioning;

import it.polimi.ingsw.am11.cards.utils.PatternCounter;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.cards.utils.enums.PatternPurpose;
import it.polimi.ingsw.am11.players.CardContainer;
import it.polimi.ingsw.am11.players.PlayerField;
import it.polimi.ingsw.am11.players.Position;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class LPatternCounter implements PatternCounter {

    private final Color primaryColor;

    private final Color secondaryColor;

    private final EnumMap<PatternPurpose, List<Corner>> cornersPurpose;

    private final Set<Position> seenPositions;

    private int numberOfPatterns;

    public LPatternCounter(Color primaryColor,
                           Color secondaryColor,
                           EnumMap<PatternPurpose, List<Corner>> cornersPurpose) {
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.cornersPurpose = cornersPurpose;
        this.seenPositions = new HashSet<>(16);
        this.numberOfPatterns = 0;
    }

    private void countNumberOfPatterns(
            @NotNull Map<Position, CardContainer> field,
            Position currentPosition,
            int numberSeen) {
        Position nextPatternPosition = PlayerField.getMovementOfPositions(
                currentPosition,
                this.cornersPurpose.get(PatternPurpose.NEXT_CHECK)
        );
        Position previousPatternPosition = PlayerField.getMovementOfPositions(
                currentPosition,
                this.cornersPurpose.get(PatternPurpose.PREVIOUS_CHECK)
        );
        Position toCompleteLPosition = PlayerField.getMovementOfPositions(
                currentPosition,
                this.cornersPurpose.get(PatternPurpose.TO_COMPLETE)
        );
        int updated_number;

        if (field.getOrDefault(currentPosition, null) == null) return;

        if (field.getOrDefault(previousPatternPosition, null) != null
                && !seenPositions.contains(previousPatternPosition)) {
            countNumberOfPatterns(field, previousPatternPosition, 0);
            return;
        }

        if (field.get(currentPosition).colorEquals(this.primaryColor)) {
            updated_number = numberSeen + 1;
        } else {
            updated_number = 0;
        }
        if (updated_number == 2) {
            CardContainer toCompleteL = field.getOrDefault(toCompleteLPosition, null);
            if (toCompleteL != null && toCompleteL.colorEquals(this.secondaryColor)) {
                numberOfPatterns += 1;
                updated_number = 0;
            } else {
                updated_number = 1;
            }
        }

        seenPositions.add(currentPosition);

        if (field.getOrDefault(nextPatternPosition, null) != null
                && !seenPositions.contains(nextPatternPosition)) {
            countNumberOfPatterns(field, nextPatternPosition, updated_number);
        }

        for (Corner corner : Corner.values()) {
            Position adjacentPosition = PlayerField.getMovementOfPositions(
                    currentPosition,
                    corner.toSingletonList());
            if (field.getOrDefault(adjacentPosition, null) != null
                    && !seenPositions.contains(adjacentPosition)) {
                countNumberOfPatterns(field, adjacentPosition, 0);
            }
        }
    }

    @Override
    public int count(@NotNull PlayerField playerField) {
        this.seenPositions.clear();
        this.numberOfPatterns = 0;
        this.countNumberOfPatterns(playerField.getCardsPositioned(), new Position(0, 0), 0);
        return this.numberOfPatterns;
    }
}
