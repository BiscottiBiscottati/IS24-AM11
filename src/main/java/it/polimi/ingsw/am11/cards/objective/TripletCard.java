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
    private Set<Position> seenPositions;
    private int numberOfPatterns;

    private TripletCard(@NotNull Builder builder) {
        super(builder, builder.colorRequirements);
        this.flippedFlag = builder.flippedFlag;
        this.colorOfPattern = builder.colorOfPattern;
        this.pattern = new CardPattern(retrievePattern());
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

    private void countPatterns(Map<Position, CardContainer> field, Position position, int numberSeen) {
        Position downLX = PlayerField.getPositionIn(Corner.DOWN_LX, position);
        Position downRX = PlayerField.getPositionIn(Corner.DOWN_RX, position);
        Position upLX = PlayerField.getPositionIn(Corner.TOP_LX, position);
        Position upRX = PlayerField.getPositionIn(Corner.TOP_RX, position);
        int updatedNumber;
        if (field.get(position).getCard().isColorEqual(this.colorOfPattern)) updatedNumber = numberSeen + 1;
        else {
            updatedNumber = 0;
        }
        seenPositions.stream()
                     .filter(position1 -> position1 == downLX)
                     .findAny()
                     .ifPresentOrElse(
                             position1 -> countPatterns(field, upRX, updatedNumber),
                             () -> {
                                 if (field.containsKey(downRX)) {
                                     countPatterns(field, downRX, 0);
                                 } else {
                                     countPatterns(field, upRX, updatedNumber);
                                 }
                             });

    }

    @Override
    @NotNull
    public ObjectiveCardType getType() {
        return ObjectiveCardType.TRIPLET;
    }

    @Override
    public int countPoints(
            PlayerField playerField) {
        if (playerField.getPlacedCardColours().get(this.colorOfPattern) < 3) return 0;
        seenPositions = new HashSet<>(16);
        numberOfPatterns = 0;
//        return this.calculatePatterns(playerField);
        return 0;
    }

    public boolean isFlipped() {
        return flippedFlag;
    }

    @Nullable
    @Override
    public CardPattern getPattern() {
        return this.pattern;
    }

    public static class Builder extends PositioningCard.Builder {

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
