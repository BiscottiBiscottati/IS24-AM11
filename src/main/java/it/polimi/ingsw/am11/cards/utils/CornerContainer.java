package it.polimi.ingsw.am11.cards.utils;

/**
 * A common interface for representing what is contained inside a corner.
 * <p>
 * Can be values of <code>Color</code>, <code>Symbol</code> or <code>Availability</code>.
 * <p>
 * If not available to cover will contain <code>Availability.NOT_USABLE</code>.
 * If available but not item inside will have <code>Availability.USABLE</code>
 *
 * @see Availability
 * @see Color
 * @see Symbol
 */
public sealed interface CornerContainer permits Availability, Color, Symbol {


    /**
     * Checks whether the corner is available to cover or not
     *
     * @return <code>true</code> if available, <code>false</code> otherwise
     */
    boolean isAvailable();
}
