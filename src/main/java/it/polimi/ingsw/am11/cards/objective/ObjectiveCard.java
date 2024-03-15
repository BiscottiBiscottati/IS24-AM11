package it.polimi.ingsw.am11.cards.objective;

import com.google.common.collect.ImmutableMap;
import it.polimi.ingsw.am11.cards.exceptions.IllegalBuildException;
import it.polimi.ingsw.am11.cards.utils.Color;
import it.polimi.ingsw.am11.cards.utils.ObjectiveCardType;
import it.polimi.ingsw.am11.cards.utils.Symbol;
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

        public abstract ObjectiveCard build() throws IllegalBuildException;
    }

}
