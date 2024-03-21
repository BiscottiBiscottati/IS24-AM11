package it.polimi.ingsw.am11.cards.objective;

import it.polimi.ingsw.am11.exceptions.IllegalBuildException;
import org.jetbrains.annotations.NotNull;

public abstract class CollectingCard extends ObjectiveCard {

    protected CollectingCard(@NotNull Builder<?> builder) {
        super(builder);
    }

    public static abstract class Builder<T extends CollectingCard>
            extends ObjectiveCard.Builder<CollectingCard> {
        protected Builder(int points) throws IllegalArgumentException {
            super(points);
        }

        @Override
        public abstract T build() throws IllegalBuildException;
    }
}
