package it.polimi.ingsw.am11.cards.objective;

import com.google.common.collect.ImmutableMap;
import it.polimi.ingsw.am11.cards.utils.CardIdentity;
import it.polimi.ingsw.am11.cards.utils.CardPattern;
import it.polimi.ingsw.am11.cards.utils.Item;
import it.polimi.ingsw.am11.cards.utils.PointsCountable;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.ObjectiveCardType;
import it.polimi.ingsw.am11.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.exceptions.IllegalCardBuildException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public abstract class ObjectiveCard implements CardIdentity, PointsCountable {
    private final int points;

    private final int id;

    protected ObjectiveCard(@NotNull Builder<?> builder) {
        this.points = builder.points;
        this.id = builder.id;
    }

    @Override
    public int getId() {
        return this.id;
    }

    public int getPoints() {
        return points;
    }

    @NotNull
    public abstract ImmutableMap<Symbol, Integer> getSymbolRequirements();

    @NotNull
    public abstract ImmutableMap<Color, Integer> getColorRequirements();

    public int hasItemRequirements(@NotNull Item item) {
        switch (item) {
            case Color color -> {
                return Objects.requireNonNull(getColorRequirements().get(color));
            }
            case Symbol symbol -> {
                return Objects.requireNonNull(getSymbolRequirements().get(symbol));
            }
        }
    }

    @NotNull
    public abstract ObjectiveCardType getType();

    @Nullable
    public CardPattern getPattern() {
        return null;
    }

    public static abstract class Builder<T extends ObjectiveCard> {
        private final int points;

        private final int id;

        protected Builder(int id, int points) throws IllegalArgumentException {
            if (points > 0) this.points = points;
            else throw new IllegalArgumentException("points cannot be less than 0!");
            this.id = id;
        }

        public abstract T build() throws IllegalCardBuildException;
    }

}
