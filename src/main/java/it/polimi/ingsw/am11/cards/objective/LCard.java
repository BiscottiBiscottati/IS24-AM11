package it.polimi.ingsw.am11.cards.objective;

import it.polimi.ingsw.am11.cards.utils.ObjectiveCardType;
import org.jetbrains.annotations.NotNull;

public class LCard extends PositioningCard {

    private final boolean isFlippedFlag;
    private final boolean isRotatedFlag;

    private LCard(@NotNull Builder builder) {
        super(builder);
        this.isFlippedFlag = builder.isFlippedFlag;
        this.isRotatedFlag = builder.isRotatedFlag;
    }

    @Override
    @NotNull
    public ObjectiveCardType getType() {
        return ObjectiveCardType.L_SHAPE;
    }


    public static class Builder extends PositioningCard.Builder {

        private boolean isFlippedFlag;
        private boolean isRotatedFlag;

        public Builder(int points) {
            super(points);
        }

        public @NotNull Builder isFlipped(boolean flippedFlag) {
            isFlippedFlag = flippedFlag;
            return this;
        }

        public @NotNull Builder isRotated(boolean rotatedFlag) {
            isRotatedFlag = rotatedFlag;
            return this;
        }

        @Override
        public @NotNull LCard build() {
            return new LCard(this);
        }
    }
}
