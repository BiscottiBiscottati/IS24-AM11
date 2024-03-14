package it.polimi.ingsw.am11.cards;

import org.jetbrains.annotations.NotNull;

public class TripletCard extends PositioningCard {
    private final boolean flippedFlag;

    protected TripletCard(@NotNull Builder builder) {
        super(builder);
        this.flippedFlag = builder.flippedFlag;
    }

    @Override
    public ObjectiveCardType getType() {
        return ObjectiveCardType.TRIPLET;
    }

    public boolean isFlipped() {
        return flippedFlag;
    }

    public static class Builder extends PositioningCard.Builder {

        private boolean flippedFlag;

        public Builder(int points) {
            super(points);
        }

        public Builder isFlipped(boolean flippedFlag) {
            this.flippedFlag = flippedFlag;
            return this;
        }

        @Override
        public ObjectiveCard build() {
            return new TripletCard(this);
        }
    }
}
