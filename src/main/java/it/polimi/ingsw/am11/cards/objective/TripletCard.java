package it.polimi.ingsw.am11.cards.objective;

import it.polimi.ingsw.am11.cards.utils.*;
import it.polimi.ingsw.am11.exceptions.IllegalBuildException;
import it.polimi.ingsw.am11.players.CardContainer;
import it.polimi.ingsw.am11.players.PlayerField;
import it.polimi.ingsw.am11.players.Position;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TripletCard extends PositioningCard {
    private final boolean flippedFlag;
    private final Color colorOfPattern;
    private final CardPattern pattern;
    private final Map<PatternPurpose, Corner> cornersPurpose;
    private Set<Position> seenPositions;
    private int numberOfPatterns;

    private TripletCard(@NotNull Builder builder) {
        super(builder, builder.colorRequirements);
        this.flippedFlag = builder.flippedFlag;
        this.colorOfPattern = builder.colorOfPattern;
        this.pattern = new CardPattern(retrievePattern());
        this.cornersPurpose = new EnumMap<>(PatternPurpose.class);
        for (PatternPurpose purpose : PatternPurpose.values()) {
            switch (purpose) {
                case UP_CHECK -> cornersPurpose.put(
                        purpose,
                        flippedFlag ? Corner.TOP_RX : Corner.TOP_LX
                );
                case DOWN_CHECK -> cornersPurpose.put(
                        purpose,
                        flippedFlag ? Corner.DOWN_LX : Corner.DOWN_RX
                );
                case ADJACENT_LX -> cornersPurpose.put(
                        purpose,
                        flippedFlag ? Corner.TOP_LX : Corner.DOWN_LX
                );
                case ADJACENT_RX -> cornersPurpose.put(
                        purpose,
                        flippedFlag ? Corner.DOWN_RX : Corner.TOP_RX
                );
            }
        }
    }

    @NotNull
    private Color[][] retrievePattern() {
        Color[][] temp = new Color[3][3];
        Arrays.stream(temp).forEach(colors -> Arrays.fill(colors, null));
        ObjectiveCardType.TRIPLET.getPositions(this.flippedFlag, false)
                                 .orElse(Set.of())
                                 .forEach(position -> temp[position.x()][position.y()] = this.colorOfPattern);
        return temp;
    }

    private void countPatterns(@NotNull Map<Position, CardContainer> field, Position position, int numberSeen) {
        Position nextPatternPosition = PlayerField.getPositionIn(
                cornersPurpose.get(PatternPurpose.UP_CHECK),
                position
        );
        Position previousPatternPosition = PlayerField.getPositionIn(
                cornersPurpose.get(PatternPurpose.DOWN_CHECK),
                position
        );
        Position adjacentLX = PlayerField.getPositionIn(
                cornersPurpose.get(PatternPurpose.ADJACENT_LX),
                position
        );
        Position adjacentRX = PlayerField.getPositionIn(
                cornersPurpose.get(PatternPurpose.ADJACENT_RX),
                position
        );
        int updatedNumber;

        if (field.getOrDefault(position, null) == null) return;

        if (field.getOrDefault(previousPatternPosition, null) != null
                && !seenPositions.contains(previousPatternPosition)) {
            countPatterns(field, previousPatternPosition, 0);
            return;
        }

        if (field.get(position).colorEquals(this.colorOfPattern)) updatedNumber = numberSeen + 1;
        else updatedNumber = 0;
        if (updatedNumber == 3) {
            numberOfPatterns += 1;
            updatedNumber = 0;
        }
        seenPositions.add(position);
        if (field.getOrDefault(nextPatternPosition, null) != null
                && !seenPositions.contains(nextPatternPosition)) {
            countPatterns(field, nextPatternPosition, updatedNumber);
        }
        if (field.getOrDefault(adjacentLX, null) != null
                && !seenPositions.contains(adjacentLX)) {
            countPatterns(field, adjacentLX, 0);
        }
        if (field.getOrDefault(adjacentRX, null) != null
                && !seenPositions.contains(adjacentRX)) {
            countPatterns(field, adjacentRX, 0);
        }


    }

    @Override
    @NotNull
    public ObjectiveCardType getType() {
        return ObjectiveCardType.TRIPLET;
    }

    @Override
    public int countPoints(
            @NotNull PlayerField playerField) {
        if (playerField.getPlacedCardColours().get(this.colorOfPattern) < 3) return 0;
        seenPositions = new HashSet<>(16);
        numberOfPatterns = 0;
        this.countPatterns(playerField.getCardsPositioned(), Position.of(0, 0), 0);
        return numberOfPatterns * this.getPoints();

    }

    public boolean isFlipped() {
        return flippedFlag;
    }

    @Nullable
    @Override
    public CardPattern getPattern() {
        return this.pattern;
    }

    private enum PatternPurpose {
        UP_CHECK,
        DOWN_CHECK,
        ADJACENT_LX,
        ADJACENT_RX
    }

    public static class Builder extends PositioningCard.Builder<TripletCard> {

        protected EnumMap<Color, Integer> colorRequirements;
        private boolean flippedFlag;
        private Color colorOfPattern;

        public Builder(int points) {
            super(points);
            flippedFlag = false;
            colorOfPattern = null;
        }

        public @NotNull Builder isFlipped(boolean flippedFlag) {
            this.flippedFlag = flippedFlag;
            return this;
        }

        public @NotNull Builder hasColor(@NotNull Color color) {
            this.colorOfPattern = color;
            this.colorRequirements = EnumMapUtils.Init(Color.class, 0);
            this.colorRequirements.put(color, 3);
            return this;
        }

        @Override
        public @NotNull TripletCard build() throws IllegalBuildException {
            if (colorOfPattern == null) throw new IllegalBuildException("Pattern need a color!");
            else return new TripletCard(this);
        }
    }
}
