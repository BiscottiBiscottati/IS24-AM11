package it.polimi.ingsw.am11.model.cards.objective.positioning;

import it.polimi.ingsw.am11.model.cards.objective.PositioningCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.cards.utils.enums.ObjectiveCardType;
import it.polimi.ingsw.am11.model.cards.utils.enums.PatternPurpose;
import it.polimi.ingsw.am11.model.cards.utils.helpers.EnumMapUtils;
import it.polimi.ingsw.am11.model.cards.utils.helpers.MatrixFiller;
import it.polimi.ingsw.am11.model.players.field.PlayerField;
import it.polimi.ingsw.am11.model.players.utils.Position;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;

public final class LCard extends PositioningCard {

    private final boolean isFlippedFlag;
    private final boolean isRotatedFlag;
    private final @Nullable Color primaryColor;
    private final @Nullable Color secondaryColor;
    private final List<List<Color>> pattern;

    private final @NotNull PatternCounter counter;

    private LCard(@NotNull Builder builder) {
        super(builder, builder.colorRequirements);
        this.isFlippedFlag = builder.isFlippedFlag;
        this.isRotatedFlag = builder.isRotatedFlag;
        this.primaryColor = builder.primaryColor;
        this.secondaryColor = builder.secondaryColor;

        EnumMap<PatternPurpose, List<Corner>> cornersPurpose = new EnumMap<>(PatternPurpose.class);
        List<Corner> cornersToGetToTop = List.of(Corner.TOP_LX, Corner.TOP_RX);
        List<Corner> cornersToGetToBottom = List.of(Corner.DOWN_LX, Corner.DOWN_RX);

        for (PatternPurpose purpose : PatternPurpose.values()) {
            switch (purpose) {
                case NEXT_CHECK -> cornersPurpose.put(
                        purpose,
                        this.isRotatedFlag ? cornersToGetToTop : cornersToGetToBottom
                );
                case PREVIOUS_CHECK -> cornersPurpose.put(
                        purpose,
                        this.isRotatedFlag ? cornersToGetToBottom : cornersToGetToTop
                );
                case TO_COMPLETE -> Arrays.stream(Corner.values())
                                          .filter(corner -> isRotatedFlag
                                                            ? (corner == Corner.TOP_LX ||
                                                               corner == Corner.TOP_RX)
                                                            : (corner == Corner.DOWN_LX ||
                                                               corner == Corner.DOWN_RX))
                                          .filter(corner -> isFlippedFlag
                                                            ? (corner == Corner.TOP_RX ||
                                                               corner == Corner.DOWN_LX)
                                                            : (corner == Corner.TOP_LX ||
                                                               corner == Corner.DOWN_RX)
                                          )
                                          .forEach(corner -> cornersPurpose.put(
                                                  purpose,
                                                  corner.toSingletonList()));
                case ADJACENT_RX, ADJACENT_LX -> cornersPurpose.put(
                        purpose,
                        null
                );
            }
        }
        this.counter = new LPatternCounter(this.primaryColor, this.secondaryColor, cornersPurpose);

        Set<Position> positions = getType().getPositions(isFlippedFlag,
                                                         isRotatedFlag).orElseThrow();

        this.pattern = MatrixFiller.fillMatrixWithNull(4, 3, Color.class);

        for (Position position : positions) {
            this.pattern.get(position.y()).set(position.x(), position.x() == 1 ?
                                                             this.primaryColor :
                                                             this.secondaryColor);
        }
    }

    @Override
    @NotNull
    public ObjectiveCardType getType() {
        return ObjectiveCardType.L_SHAPE;
    }

    @Override
    public int countPoints(@NotNull PlayerField playerField) {
        assert this.primaryColor != null;
        if (playerField.getNumberOf(this.primaryColor) < 2) return 0;
        assert this.secondaryColor != null;
        if (playerField.getNumberOf(this.secondaryColor) < 1) return 0;
        return this.counter.count(playerField) * this.getPoints();
    }

    public boolean isFlipped() {
        return this.isFlippedFlag;
    }

    public boolean isRotated() {
        return this.isRotatedFlag;
    }

    @Override
    public @NotNull List<List<Color>> getPattern() {

        return pattern;
    }

    public static class Builder extends PositioningCard.Builder<LCard> {

        private final @NotNull EnumMap<Color, Integer> colorRequirements;
        private boolean isFlippedFlag;
        private boolean isRotatedFlag;
        private @Nullable Color primaryColor;
        private @Nullable Color secondaryColor;

        public Builder(int id, int points) {
            super(id, points);
            this.primaryColor = null;
            this.secondaryColor = null;
            this.colorRequirements = EnumMapUtils.init(Color.class, 0);
        }

        public @NotNull Builder isFlipped(boolean flippedFlag) {
            this.isFlippedFlag = flippedFlag;
            return this;
        }

        public @NotNull Builder isRotated(boolean rotatedFlag) {
            this.isRotatedFlag = rotatedFlag;
            return this;
        }

        public @NotNull Builder hasPrimaryColor(@NotNull Color color) {
            if (this.primaryColor != null) this.colorRequirements.put(this.primaryColor, 0);
            this.primaryColor = color;
            this.colorRequirements.put(color, 2);
            return this;
        }

        public @NotNull Builder hasSecondaryColor(@NotNull Color color) {
            if (this.secondaryColor != null) this.colorRequirements.put(this.secondaryColor, 0);
            this.secondaryColor = color;
            this.colorRequirements.put(color, 1);
            return this;
        }

        @Override
        public @NotNull LCard build() {
            return new LCard(this);
        }
    }
}
