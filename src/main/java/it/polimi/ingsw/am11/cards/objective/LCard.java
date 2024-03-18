package it.polimi.ingsw.am11.cards.objective;

import it.polimi.ingsw.am11.cards.utils.Color;
import it.polimi.ingsw.am11.cards.utils.ObjectiveCardType;
import it.polimi.ingsw.am11.cards.utils.Symbol;
import it.polimi.ingsw.am11.players.CardContainer;
import it.polimi.ingsw.am11.players.Position;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

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

    @Override
    public int countPoints(
            Map<Position, CardContainer> field,
            Map<Symbol, Integer> symbolOccurrences,
            Map<Color, Integer> colorOccurrences,
            Map<Color, Integer> cardColorOccurrences) {
        return 0;
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
