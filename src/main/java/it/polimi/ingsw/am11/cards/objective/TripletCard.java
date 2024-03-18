package it.polimi.ingsw.am11.cards.objective;

import com.google.common.collect.ImmutableMap;
import it.polimi.ingsw.am11.cards.exceptions.IllegalBuildException;
import it.polimi.ingsw.am11.cards.utils.*;
import it.polimi.ingsw.am11.players.CardContainer;
import it.polimi.ingsw.am11.players.Position;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class TripletCard extends PositioningCard {
    private final boolean flippedFlag;
    private final Color colorOfPattern;

    private TripletCard(@NotNull Builder builder) {
        super(builder, builder.colorRequirements);
        this.flippedFlag = builder.flippedFlag;
        this.colorOfPattern = builder.colorOfPattern;
    }

    @Override
    public @NotNull ImmutableMap<Color, Integer> getColorRequirements() {
        return null;
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

    @Nullable
    @Override
    public CardPattern getPattern() {
        return null;
    }

    public static class Builder extends PositioningCard.Builder {

        protected EnumMap<Color, Integer> colorRequirements;
        private boolean flippedFlag;
        private Color colorOfPattern;

        public Builder(int points) {
            super(points);
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
