package it.polimi.ingsw.am11.cards.objective;

import com.google.common.collect.ImmutableMap;
import it.polimi.ingsw.am11.cards.utils.CardPattern;
import it.polimi.ingsw.am11.cards.utils.Color;
import it.polimi.ingsw.am11.cards.utils.ObjectiveCardType;
import it.polimi.ingsw.am11.cards.utils.Symbol;
import it.polimi.ingsw.am11.exceptions.IllegalBuildException;
import it.polimi.ingsw.am11.players.PlayerField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public abstract class ObjectiveCard {
    private final int points;


    protected ObjectiveCard(@NotNull Builder<?> builder) {
        this.points = builder.points;
    }

    public int getPoints() {
        return points;
    }

    @NotNull
    public abstract ImmutableMap<Symbol, Integer> getSymbolRequirements();

    @NotNull
    public abstract ImmutableMap<Color, Integer> getColorRequirements();

    @NotNull
    public abstract ObjectiveCardType getType();

    public abstract int countPoints(
            PlayerField playerField);

    @Nullable
    public CardPattern getPattern() {
        return null;
    }

    public static abstract class Builder<T extends ObjectiveCard> {
        private final int points;

        protected Builder(int points) throws IllegalArgumentException {
            if (points > 0) this.points = points;
            else throw new IllegalArgumentException("points cannot be less than 0!");
        }

        public abstract T build() throws IllegalBuildException;
    }

}
