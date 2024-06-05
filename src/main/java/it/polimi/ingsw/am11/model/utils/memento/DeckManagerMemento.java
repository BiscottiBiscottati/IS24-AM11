package it.polimi.ingsw.am11.model.utils.memento;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public record DeckManagerMemento(@NotNull DeckMemento resourceDeck,
                                 @NotNull DeckMemento goldDeck,
                                 @NotNull DeckMemento starterDeck,
                                 @NotNull DeckMemento objectiveDeck)
        implements Serializable {
}
