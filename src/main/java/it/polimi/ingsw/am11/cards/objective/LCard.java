package it.polimi.ingsw.am11.cards.objective;

public class LCard extends PositioningCard {

    private final boolean isFlippedFlag;
    private final boolean isRotatedFlag;

    private LCard(Builder builder) {
        super(builder);
        isFlippedFlag = builder.isFlippedFlag;
        isRotatedFlag = builder.isRotatedFlag;
    }

    @Override
    public ObjectiveCardType getType() {
        return ObjectiveCardType.L_SHAPE;
    }


    public static class Builder extends PositioningCard.Builder {

        private boolean isFlippedFlag;
        private boolean isRotatedFlag;

        public Builder(int points) {
            super(points);
        }

        public Builder isFlipped(boolean flippedFlag) {
            isFlippedFlag = flippedFlag;
            return this;
        }

        public Builder isRotated(boolean rotatedFlag) {
            isRotatedFlag = rotatedFlag;
            return this;
        }

        @Override
        public ObjectiveCard build() {
            return new LCard(this);
        }
    }
}
