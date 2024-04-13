package it.polimi.ingsw.am11.cards.utils.enums;

import it.polimi.ingsw.am11.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.cards.utils.DatabaseSearchable;
import it.polimi.ingsw.am11.cards.utils.Item;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Can represent either the color of a card or the colored symbols contained in a corner or center of a card.
 * <p>
 * Can be <code>RED</code>, <code>BLUE</code>, <code>GREEN</code> or <code>PURPLE</code>.
 */
public enum Color implements CornerContainer, Item, DatabaseSearchable {
    RED("red"),
    BLUE("blue"),
    GREEN("green"),
    PURPLE("purple");

    private final String columnName;

    Color(String columnName) {
        this.columnName = columnName;
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

    @Override
    public String getColumnName() {
        return this.columnName;
    }


}
