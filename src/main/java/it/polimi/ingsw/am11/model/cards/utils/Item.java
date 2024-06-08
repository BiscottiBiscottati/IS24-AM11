package it.polimi.ingsw.am11.model.cards.utils;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.Symbol;

/**
 * A common interface for <code>Color</code> and <code>Symbol</code>.
 *
 * @see Color
 * @see Symbol
 */
public sealed interface Item permits Color, Symbol {

    /**
     * This method is part of the <code>Item</code> interface which is implemented by the Color and
     * Symbol enums.
     * <p>
     * It is used to get the column name associated with the item.
     * <p>
     * The column name is a string representation of the item which can be used in database
     * operations.
     *
     * @return String - The column name of the item.
     */
    String getColumnName();
}