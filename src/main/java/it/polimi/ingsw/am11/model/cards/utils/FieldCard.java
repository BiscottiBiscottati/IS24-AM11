package it.polimi.ingsw.am11.model.cards.utils;

import it.polimi.ingsw.am11.model.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.model.cards.starter.StarterCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * A common interface for representing all cards that can be positioned on the field.
 * <p>
 * Can be a <code>PlayableCard</code> or a <code>StarterCard</code>.
 *
 * @see PlayableCard
 * @see StarterCard
 */
public sealed interface FieldCard extends CardIdentity
        permits PlayableCard, StarterCard {
    /**
     * Checks whether the card's color is equal to the parameter.
     *
     * @param color the color to check
     * @return true if equal, false otherwise
     */
    boolean isColorEqual(@NotNull GameColor color);

    /**
     * This method is part of the <code>FieldCard</code> interface.
     * <p>
     * It checks whether a specific corner of the card is available or not.
     * <p>
     * Retro mode can alter the availability of corners based on specific game rules.
     *
     * @param corner  Corner - The corner to check for availability.
     * @param isRetro boolean - Whether to check in retro mode or not.
     * @return boolean - True if the corner is available, false otherwise.
     */
    boolean isAvailable(@NotNull Corner corner, boolean isRetro);

    /**
     * This method is part of the <code>FieldCard</code> interface.
     * <p>
     * It is used to get the <code>CornerContainer</code> object associated with a specific corner
     * of the card.
     * <p>
     * The CornerContainer object can contain a value of Color, Symbol, or Availability.
     * <p>
     * Retro mode can alter the contents of the corner based on specific game rules.
     * <p>
     * The method can return null if the corner does not contain an item or is not available.
     *
     * @param corner  Corner - The corner to check for an item.
     * @param isRetro boolean - Whether to check in retro mode or not.
     * @return CornerContainer - The CornerContainer object associated with the corner or null if
     * the corner is not available or does not contain an item.
     */
    @NotNull
    CornerContainer getItemCorner(@NotNull Corner corner, boolean isRetro);

    @NotNull
    Set<GameColor> getCenter(boolean isRetro);
}
