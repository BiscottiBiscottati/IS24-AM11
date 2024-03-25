package it.polimi.ingsw.am11.cards.objective;

import it.polimi.ingsw.am11.cards.utils.*;
import it.polimi.ingsw.am11.exceptions.IllegalBuildException;
import it.polimi.ingsw.am11.players.PlayerField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Set;

public class TripletCard extends PositioningCard {
    private final boolean flippedFlag;
    private final Color colorOfPattern;
    private final CardPattern pattern;
    private final PatternCounter counter;

    private TripletCard(@NotNull Builder builder) {
        super(builder, builder.colorRequirements);
        this.flippedFlag = builder.flippedFlag;
        this.colorOfPattern = builder.colorOfPattern;
        this.pattern = new CardPattern(retrievePattern());
        EnumMap<PatternPurpose, Corner> cornersPurpose = new EnumMap<>(PatternPurpose.class);
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
        this.counter = new TripletPatternCounter(this.colorOfPattern, cornersPurpose);
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

    @Override
    @NotNull
    public ObjectiveCardType getType() {
        return ObjectiveCardType.TRIPLET;
    }

    @Override
    public int countPoints(
            @NotNull PlayerField playerField) {
        if (playerField.getPlacedCardColours().get(this.colorOfPattern) < 3) return 0;
        else return this.counter.count(playerField) * this.getPoints();

    }

    public boolean isFlipped() {
        return flippedFlag;
    }

    @Nullable
    @Override
    public CardPattern getPattern() {
        return this.pattern;
    }


    public static class Builder extends PositioningCard.Builder<TripletCard> {

        protected EnumMap<Color, Integer> colorRequirements;
        private boolean flippedFlag;
        private Color colorOfPattern;

        public Builder(int id, int points) {
            super(id, points);
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
