package it.polimi.ingsw.am11.model.cards.objective.positioning;

import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.PatternPurpose;
import it.polimi.ingsw.am11.model.players.field.PlayerField;
import it.polimi.ingsw.am11.model.players.field.PositionManager;
import it.polimi.ingsw.am11.model.players.utils.CardContainer;
import it.polimi.ingsw.am11.model.players.utils.Position;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TripletPatternCounter implements PatternCounter {

    private final GameColor tripletColor;
    private final EnumMap<PatternPurpose, Corner> cornersPurpose;
    private final @NotNull Set<Position> seenPositions;
    private int numberOfPatterns;

    protected TripletPatternCounter(GameColor color,
                                    EnumMap<PatternPurpose, Corner> cornersPurpose) {
        this.seenPositions = new HashSet<>(16);
        this.numberOfPatterns = 0;
        this.tripletColor = color;
        this.cornersPurpose = cornersPurpose;
    }

    private void countNumberOfPatterns(
            @NotNull Map<Position, CardContainer> field,
            @NotNull Position currentPosition,
            int numberSeen) {
        Position nextPatternPosition = PositionManager.getPositionIn(
                currentPosition, cornersPurpose.get(PatternPurpose.NEXT_CHECK)
        );
        Position previousPatternPosition = PositionManager.getPositionIn(
                currentPosition, cornersPurpose.get(PatternPurpose.PREVIOUS_CHECK)
        );
        Position adjacentLX = PositionManager.getPositionIn(
                currentPosition, cornersPurpose.get(PatternPurpose.ADJACENT_LX)
        );
        Position adjacentRX = PositionManager.getPositionIn(
                currentPosition, cornersPurpose.get(PatternPurpose.ADJACENT_RX)
        );
        int updatedNumber;

        if (field.getOrDefault(currentPosition, null) == null) return;

        if (field.getOrDefault(previousPatternPosition, null) != null
            && ! seenPositions.contains(previousPatternPosition)) {
            countNumberOfPatterns(field, previousPatternPosition, 0);
            return;
        }

        if (field.get(currentPosition).isColorEquals(this.tripletColor))
            updatedNumber = numberSeen + 1;
        else updatedNumber = 0;
        if (updatedNumber == 3) {
            numberOfPatterns += 1;
            updatedNumber = 0;
        }
        seenPositions.add(currentPosition);
        if (field.getOrDefault(nextPatternPosition, null) != null
            && ! seenPositions.contains(nextPatternPosition)) {
            countNumberOfPatterns(field, nextPatternPosition, updatedNumber);
        }
        if (field.getOrDefault(adjacentLX, null) != null
            && ! seenPositions.contains(adjacentLX)) {
            countNumberOfPatterns(field, adjacentLX, 0);
        }
        if (field.getOrDefault(adjacentRX, null) != null
            && ! seenPositions.contains(adjacentRX)) {
            countNumberOfPatterns(field, adjacentRX, 0);
        }
    }

    @Override
    public int count(@NotNull PlayerField playerField) {
        this.seenPositions.clear();
        this.numberOfPatterns = 0;
        this.countNumberOfPatterns(playerField.getCardsPositioned(), Position.of(0, 0), 0);
        return this.numberOfPatterns;
    }
}
