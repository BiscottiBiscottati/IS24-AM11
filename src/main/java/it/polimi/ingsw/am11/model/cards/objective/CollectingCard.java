package it.polimi.ingsw.am11.model.cards.objective;

import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import org.jetbrains.annotations.NotNull;

public abstract class CollectingCard extends ObjectiveCard {

    protected CollectingCard(@NotNull Builder<?> builder) {
        super(builder);
    }

    public static abstract class Builder<T extends CollectingCard>
            extends ObjectiveCard.Builder<CollectingCard> {
        protected Builder(int id, int points) throws IllegalArgumentException {
            super(id, points);
        }

        @Override
        public abstract T build() throws IllegalCardBuildException;
    }
}
