package it.polimi.ingsw.am11.cards.objective;

import it.polimi.ingsw.am11.cards.utils.ObjectiveCardType;
import org.jetbrains.annotations.NotNull;

public class TripletCard extends PositioningCard {
    private final boolean flippedFlag;

    private TripletCard(@NotNull Builder builder) {
        super(builder);
        this.flippedFlag = builder.flippedFlag;
    }

    @Override
    public @NotNull ObjectiveCardType getType() {
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

        public @NotNull Builder isFlipped(boolean flippedFlag) {
            this.flippedFlag = flippedFlag;
            return this;
        }

        @Override
        public @NotNull TripletCard build() {
            return new TripletCard(this);
        }
    }
}
