package it.polimi.ingsw.am11.model.cards.utils.enums;

import it.polimi.ingsw.am11.model.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.model.cards.utils.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Represents whether another card can cover a corner of a card.
 * <p>
 * Can be <code>USABLE</code> OR <code>NOT_USABLE</code>
 */
public enum Availability implements CornerContainer {
    USABLE(true, "+"),
    NOT_USABLE(false, "X");

    private final boolean isAvailable;
    private final String TUICode;

    Availability(boolean isAvailable, String TUICode) {
        this.isAvailable = isAvailable;
        this.TUICode = TUICode;
    }

    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public @NotNull Optional<Item> getItem() {
        return Optional.empty();
    }

    @Override
    public String getTUICode() {
        return TUICode;
    }


}
