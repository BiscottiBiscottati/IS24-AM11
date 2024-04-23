package it.polimi.ingsw.am11.model.cards.utils;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.Symbol;


//TODO may be deleted depending on the usage

/**
 * A common interface for <code>Color</code> and <code>Symbol</code>.
 *
 * @see Color
 * @see Symbol
 */
public sealed interface Item permits Color, Symbol {
}