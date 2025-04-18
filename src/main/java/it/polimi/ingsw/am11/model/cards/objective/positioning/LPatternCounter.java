package it.polimi.ingsw.am11.model.cards.objective.positioning;

import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.PatternPurpose;
import it.polimi.ingsw.am11.model.players.field.PlayerField;
import it.polimi.ingsw.am11.model.players.field.PositionManager;
import it.polimi.ingsw.am11.model.players.utils.CardContainer;
import it.polimi.ingsw.am11.model.players.utils.Position;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class LPatternCounter implements PatternCounter {

    private final GameColor primaryColor;

    private final GameColor secondaryColor;

    private final EnumMap<PatternPurpose, List<Corner>> cornersPurpose;

    private final @NotNull Set<Position> seenPositions;

    private int numberOfPatterns;

    public LPatternCounter(GameColor primaryColor,
                           GameColor secondaryColor,
                           EnumMap<PatternPurpose, List<Corner>> cornersPurpose) {
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.cornersPurpose = cornersPurpose;
        this.seenPositions = new HashSet<>(16);
        this.numberOfPatterns = 0;
    }

    private void countNumberOfPatterns(
            @NotNull Map<Position, CardContainer> field,
            @NotNull Position currentPosition,
            int numberSeen) {
        Position nextPatternPosition = PositionManager.getMovementOfPositions(
                currentPosition,
                this.cornersPurpose.get(PatternPurpose.NEXT_CHECK)
        );
        Position previousPatternPosition = PositionManager.getMovementOfPositions(
                currentPosition,
                this.cornersPurpose.get(PatternPurpose.PREVIOUS_CHECK)
        );
        Position toCompleteLPosition = PositionManager.getMovementOfPositions(
                currentPosition,
                this.cornersPurpose.get(PatternPurpose.TO_COMPLETE)
        );
        int updated_number;

        if (field.getOrDefault(currentPosition, null) == null) return;

        if (field.getOrDefault(previousPatternPosition, null) != null
            && ! seenPositions.contains(previousPatternPosition)) {
            countNumberOfPatterns(field, previousPatternPosition, 0);
            return;
        }

        if (field.get(currentPosition).isColorEquals(this.primaryColor)) {
            updated_number = numberSeen + 1;
        } else {
            updated_number = 0;
        }
        if (updated_number == 2) {
            CardContainer toCompleteL = field.getOrDefault(toCompleteLPosition, null);
            if (toCompleteL != null && toCompleteL.isColorEquals(this.secondaryColor)) {
                numberOfPatterns += 1;
                updated_number = 0;
            } else {
                updated_number = 1;
            }
        }

        seenPositions.add(currentPosition);

        if (field.getOrDefault(nextPatternPosition, null) != null
            && ! seenPositions.contains(nextPatternPosition)) {
            countNumberOfPatterns(field, nextPatternPosition, updated_number);
        }

        for (Corner corner : Corner.values()) {
            Position adjacentPosition = PositionManager.getMovementOfPositions(
                    currentPosition,
                    List.of(corner));
            if (field.getOrDefault(adjacentPosition, null) != null
                && ! seenPositions.contains(adjacentPosition)) {
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
