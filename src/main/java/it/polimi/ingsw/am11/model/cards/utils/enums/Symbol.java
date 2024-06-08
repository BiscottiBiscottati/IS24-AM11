package it.polimi.ingsw.am11.model.cards.utils.enums;

import it.polimi.ingsw.am11.model.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.model.cards.utils.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Represents the uncolored symbols contained in the corners of a card.
 * <p>
 * Can be <code>FEATHER</code>, <code>GLASS</code> or <code>PAPER</code>.
 */
public enum Symbol implements CornerContainer, Item {
    FEATHER("feather", "F"),
    GLASS("glass", "I"),
    PAPER("paper", "S");

    private final String columnName;
    private final String TUIRepresentation;

    Symbol(String columnName, String TUIRepresentation) {
        this.columnName = columnName;
        this.TUIRepresentation = TUIRepresentation;
    }

    @Override
    public String getColumnName() {
        return this.columnName;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public @NotNull Optional<Item> getItem() {
        return Optional.of(this);
    }

    @Override
    public String getTUICode() {
        return TUIRepresentation;
    }

}
