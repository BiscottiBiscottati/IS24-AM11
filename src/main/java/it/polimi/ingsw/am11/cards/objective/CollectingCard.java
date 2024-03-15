package it.polimi.ingsw.am11.cards.objective;

import org.jetbrains.annotations.NotNull;

public abstract class CollectingCard extends ObjectiveCard {

    protected CollectingCard(@NotNull Builder builder) {
        super(builder);
    }

    public static abstract class Builder extends ObjectiveCard.Builder {
        protected Builder(int points) {
            super(points);
        }
    }
}
