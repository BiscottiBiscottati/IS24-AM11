package it.polimi.ingsw.am11.cards.objective;

import it.polimi.ingsw.am11.cards.util.Color;
import it.polimi.ingsw.am11.cards.util.ObjectiveCardType;
import it.polimi.ingsw.am11.cards.util.Symbol;

import java.util.EnumMap;

public class ColorCollectCard extends CollectingCard {
    private final EnumMap<Color, Integer> colorToCollect;


    private ColorCollectCard(Builder builder) {
        super(builder);
        this.colorToCollect = builder.colorToCollect;

    }

    @Override
    public EnumMap<Symbol, Integer> getSymbolRequirements() {
        return null;
    }

    @Override
    public EnumMap<Color, Integer> getColorRequirements() {
        return this.colorToCollect;
    }

    @Override
    public ObjectiveCardType getType() {
        return ObjectiveCardType.COLOR_COLLECT;
    }

    public static class Builder extends CollectingCard.Builder {

        private final EnumMap<Color, Integer> colorToCollect;

        public Builder(int points) {
            super(points);
            this.colorToCollect = new EnumMap<>(Color.class);
        }

        public Builder hasColor(Color color, int quantity) {
            this.colorToCollect.put(color, quantity);
            return this;
        }

        public Builder hasColor(Color color) {
            this.colorToCollect.put(color, 1);
            return this;
        }

        @Override
        public ObjectiveCard build() {
            return new ColorCollectCard(this);
        }
    }
}
