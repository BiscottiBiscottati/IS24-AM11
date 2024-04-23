package it.polimi.ingsw.am11.model.cards.utils.enums;

import it.polimi.ingsw.am11.model.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.model.cards.utils.Item;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Represents the uncolored symbols contained in the corners of a card.
 * <p>
 * Can be <code>FEATHER</code>, <code>GLASS</code> or <code>PAPER</code>.
 */
public enum Symbol implements CornerContainer, Item {
    FEATHER("feather"),
    GLASS("glass"),
    PAPER("paper");

    private final String columnName;

    Symbol(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return this.columnName;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Contract(value = " -> new", pure = true)
    @Override
    public @NotNull Optional<Item> getItem() {
        return Optional.of(this);
    }


}
