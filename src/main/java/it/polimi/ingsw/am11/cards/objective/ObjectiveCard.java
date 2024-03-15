package it.polimi.ingsw.am11.cards.objective;

import com.google.common.collect.ImmutableMap;
import it.polimi.ingsw.am11.cards.util.Color;
import it.polimi.ingsw.am11.cards.util.ObjectiveCardType;
import it.polimi.ingsw.am11.cards.util.Symbol;
import org.jetbrains.annotations.NotNull;

public abstract class ObjectiveCard {
    private final int points;

    protected ObjectiveCard(@NotNull Builder builder) {
        this.points = builder.points;
    }

    public abstract ImmutableMap<Symbol, Integer> getSymbolRequirements();

    public abstract ImmutableMap<Color, Integer> getColorRequirements();

    public int getPoints() {
        return points;
    }

    public abstract ObjectiveCardType getType();

    public static abstract class Builder {
        protected final int points;

        protected Builder(int points) {
            this.points = points;
        }

        public abstract ObjectiveCard build();
    }

}
