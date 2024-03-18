package it.polimi.ingsw.am11.cards.objective;

import com.google.common.collect.ImmutableMap;
import it.polimi.ingsw.am11.cards.exceptions.IllegalBuildException;
import it.polimi.ingsw.am11.cards.utils.Color;
import it.polimi.ingsw.am11.cards.utils.ObjectiveCardType;
import it.polimi.ingsw.am11.cards.utils.Symbol;
import it.polimi.ingsw.am11.players.CardContainer;
import it.polimi.ingsw.am11.players.Position;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Getter
public abstract class ObjectiveCard {
    private final int points;

    protected ObjectiveCard(@NotNull Builder builder) {
        this.points = builder.points;
    }

    @NotNull
    public abstract ImmutableMap<Symbol, Integer> getSymbolRequirements();

    @NotNull
    public abstract ImmutableMap<Color, Integer> getColorRequirements();

    @NotNull
    public abstract ObjectiveCardType getType();

    public abstract int countPoints(
            Map<Position, CardContainer> field,
            Map<Symbol, Integer> symbolOccurrences,
            Map<Color, Integer> colorOccurrences,
            Map<Color, Integer> cardColorOccurrences
    );

    public static abstract class Builder {
        private final int points;

        protected Builder(int points) throws IllegalArgumentException {
            if (points > 0) this.points = points;
            else throw new IllegalArgumentException("points cannot be less than 0!");
        }

        public abstract ObjectiveCard build() throws IllegalBuildException;
    }

}
