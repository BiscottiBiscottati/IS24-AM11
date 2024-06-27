package it.polimi.ingsw.am11.model.cards.objective.positioning;

import it.polimi.ingsw.am11.model.cards.objective.PositioningCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.ObjectiveCardType;
import it.polimi.ingsw.am11.model.cards.utils.enums.PatternPurpose;
import it.polimi.ingsw.am11.model.cards.utils.helpers.EnumMapUtils;
import it.polimi.ingsw.am11.model.cards.utils.helpers.MatrixFiller;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.model.players.field.PlayerField;
import it.polimi.ingsw.am11.model.players.utils.Position;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.List;
import java.util.Set;

public final class TripletCard extends PositioningCard {
    private final boolean flippedFlag;
    private final @Nullable GameColor colorOfPattern;
    private final @NotNull PatternCounter counter;
    private final @NotNull List<List<GameColor>> pattern;

    private TripletCard(@NotNull Builder builder) {
        super(builder, builder.colorRequirements);
        this.flippedFlag = builder.flippedFlag;
        this.colorOfPattern = builder.colorOfPattern;
        EnumMap<PatternPurpose, Corner> cornersPurpose = new EnumMap<>(PatternPurpose.class);
        for (PatternPurpose purpose : PatternPurpose.values()) {
            switch (purpose) {
                case NEXT_CHECK -> cornersPurpose.put(
                        purpose,
                        flippedFlag ? Corner.TOP_RX : Corner.TOP_LX
                );
                case PREVIOUS_CHECK -> cornersPurpose.put(
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

        Set<Position> positions = getType().getPositions(flippedFlag, false).orElseThrow();

        this.pattern = MatrixFiller.fillMatrixWithNull(4, 3, GameColor.class);

        positions.forEach(position -> {
            this.pattern.get(position.y()).set(position.x(), colorOfPattern);
        });
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

    @Override
    public @NotNull List<List<GameColor>> getPattern() {
        return pattern;
    }

    public static class Builder extends PositioningCard.Builder<TripletCard> {

        private EnumMap<GameColor, Integer> colorRequirements;
        private boolean flippedFlag;
        private @Nullable GameColor colorOfPattern;

        public Builder(int id, int points) {
            super(id, points);
            flippedFlag = false;
            colorOfPattern = null;
        }

        public @NotNull Builder isFlipped(boolean flippedFlag) {
            this.flippedFlag = flippedFlag;
            return this;
        }

        public @NotNull Builder hasColor(@NotNull GameColor color) {
            this.colorOfPattern = color;
            this.colorRequirements = EnumMapUtils.init(GameColor.class, 0);
            this.colorRequirements.put(color, 3);
            return this;
        }

        @Override
        public @NotNull TripletCard build() throws IllegalCardBuildException {
            if (colorOfPattern == null)
                throw new IllegalCardBuildException("Pattern need a color!");
            else return new TripletCard(this);
        }
    }
}
