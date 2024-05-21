package it.polimi.ingsw.am11.model.cards.utils.enums;

import it.polimi.ingsw.am11.model.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.model.cards.utils.DatabaseSearchable;
import it.polimi.ingsw.am11.model.cards.utils.Item;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Can represent either the color of a card or the colored symbols contained in a corner or center
 * of a card.
 * <p>
 * Can be <code>RED</code>, <code>BLUE</code>, <code>GREEN</code> or <code>PURPLE</code>.
 */
public enum Color implements CornerContainer, Item, DatabaseSearchable {
    RED("red", "R"),
    BLUE("blue", "B"),
    GREEN("green", "G"),
    PURPLE("purple", "P");

    private final String columnName;
    private final String TUIRepresentation;

    Color(String columnName, String TUIRepresentation) {
        this.columnName = columnName;
        this.TUIRepresentation = TUIRepresentation;
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
    public String getTUICode() {
        return TUIRepresentation;
    }

    @Override
    public String getColumnName() {
        return this.columnName;
    }
}
