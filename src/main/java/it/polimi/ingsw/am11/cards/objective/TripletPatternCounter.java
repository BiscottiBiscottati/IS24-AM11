package it.polimi.ingsw.am11.cards.objective;

import it.polimi.ingsw.am11.cards.utils.Color;
import it.polimi.ingsw.am11.cards.utils.Corner;
import it.polimi.ingsw.am11.cards.utils.PatternCounter;
import it.polimi.ingsw.am11.cards.utils.PatternPurpose;
import it.polimi.ingsw.am11.players.CardContainer;
import it.polimi.ingsw.am11.players.PlayerField;
import it.polimi.ingsw.am11.players.Position;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TripletPatternCounter implements PatternCounter {

    private final Color tripletColor;
    private final EnumMap<PatternPurpose, Corner> cornersPurpose;
    private final Set<Position> seenPositions;
    private int numberOfPatterns;

    protected TripletPatternCounter(Color color, EnumMap<PatternPurpose, Corner> cornersPurpose) {
        this.seenPositions = new HashSet<>(16);
        this.numberOfPatterns = 0;
        this.tripletColor = color;
        this.cornersPurpose = cornersPurpose;
    }

    public void countNumberOfPatterns(Map<Position, CardContainer> field, Position currentPosition, int numberSeen) {
        Position nextPatternPosition = PlayerField.getPositionIn(
                cornersPurpose.get(PatternPurpose.UP_CHECK),
                currentPosition
        );
        Position previousPatternPosition = PlayerField.getPositionIn(
                cornersPurpose.get(PatternPurpose.DOWN_CHECK),
                currentPosition
        );
        Position adjacentLX = PlayerField.getPositionIn(
                cornersPurpose.get(PatternPurpose.ADJACENT_LX),
                currentPosition
        );
        Position adjacentRX = PlayerField.getPositionIn(
                cornersPurpose.get(PatternPurpose.ADJACENT_RX),
                currentPosition
        );
        int updatedNumber;

        if (field.getOrDefault(currentPosition, null) == null) return;

        if (field.getOrDefault(previousPatternPosition, null) != null
                && !seenPositions.contains(previousPatternPosition)) {
            countNumberOfPatterns(field, previousPatternPosition, 0);
            return;
        }

        if (field.get(currentPosition).colorEquals(this.tripletColor)) updatedNumber = numberSeen + 1;
        else updatedNumber = 0;
        if (updatedNumber == 3) {
            numberOfPatterns += 1;
            updatedNumber = 0;
        }
        seenPositions.add(currentPosition);
        if (field.getOrDefault(nextPatternPosition, null) != null
                && !seenPositions.contains(nextPatternPosition)) {
            countNumberOfPatterns(field, nextPatternPosition, updatedNumber);
        }
        if (field.getOrDefault(adjacentLX, null) != null
                && !seenPositions.contains(adjacentLX)) {
            countNumberOfPatterns(field, adjacentLX, 0);
        }
        if (field.getOrDefault(adjacentRX, null) != null
                && !seenPositions.contains(adjacentRX)) {
            countNumberOfPatterns(field, adjacentRX, 0);
        }
    }

    @Override
    public int count(PlayerField playerField) {
        this.seenPositions.clear();
        this.numberOfPatterns = 0;
        this.countNumberOfPatterns(playerField.getCardsPositioned(), Position.of(0, 0), 0);
        return this.numberOfPatterns;
    }
}
