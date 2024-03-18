package it.polimi.ingsw.am11.cards.objective;

import it.polimi.ingsw.am11.cards.utils.Color;
import it.polimi.ingsw.am11.cards.utils.ObjectiveCardType;
import it.polimi.ingsw.am11.cards.utils.Symbol;
import it.polimi.ingsw.am11.players.CardContainer;
import it.polimi.ingsw.am11.players.Position;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class TripletCard extends PositioningCard {
    private final boolean flippedFlag;

    private TripletCard(@NotNull Builder builder) {
        super(builder);
        this.flippedFlag = builder.flippedFlag;
    }

    @Override
    @NotNull
    public ObjectiveCardType getType() {
        return ObjectiveCardType.TRIPLET;
    }

    @Override
    public int countPoints(
            Map<Position, CardContainer> field,
            Map<Symbol, Integer> symbolOccurrences,
            Map<Color, Integer> colorOccurrences,
            Map<Color, Integer> cardColorOccurrences
    ) {
        return 0;
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
