package it.polimi.ingsw.am11.cards.utils;

import com.google.common.base.Enums;
import it.polimi.ingsw.am11.cards.utils.enums.Availability;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Symbol;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * A common interface for representing what is contained inside a corner.
 * <p>
 * Can be values of <code>Color</code>, <code>Symbol</code> or <code>Availability</code>.
 * <p>
 * If not available to cover will contain <code>Availability.NOT_USABLE</code>. If available but not
 * item inside will have <code>Availability.USABLE</code>
 *
 * @see Availability
 * @see Color
 * @see Symbol
 */
public sealed interface CornerContainer permits Availability, Color, Symbol {

    /**
     * A method to get the corner container with the given name.
     * <p>
     * Used for querying the corner container by name in the SQLite database.
     *
     * @param name The name of the corner container
     * @return The corner container with the given name
     */
    static @NotNull CornerContainer of(@NotNull String name) {
        if (Enums.getIfPresent(Availability.class, name).isPresent()) {
            return Availability.valueOf(name);
        } else if (Enums.getIfPresent(Color.class, name).isPresent()) {
            return Color.valueOf(name);
        } else if (Enums.getIfPresent(Symbol.class, name).isPresent()) {
            return Symbol.valueOf(name);
        } else {
            throw new IllegalArgumentException("Invalid corner container name: " + name);
        }
    }

    /**
     * Checks whether the corner is available to cover or not
     *
     * @return <code>true</code> if available, <code>false</code> otherwise
     */
    boolean isAvailable();

    Optional<Item> getItem();
}
