package it.polimi.ingsw.am11.model.utils.memento;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public record DeckMemento(@NotNull List<Integer> cards)
        implements Serializable {
}
