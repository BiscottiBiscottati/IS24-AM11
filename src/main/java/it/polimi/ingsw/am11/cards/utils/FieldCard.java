package it.polimi.ingsw.am11.cards.utils;

import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;

/**
 * A common interface for representing all cards that can be positioned on the field.
 * <p>
 * Can be a <code>PlayableCard</code> or a <code>StarterCard</code>.
 *
 * @see PlayableCard
 * @see StarterCard
 */
public sealed interface FieldCard permits PlayableCard, StarterCard {
    /**
     * Checks whether the card's color is equal to the parameter.
     *
     * @param color the color to check
     * @return true if equal, false otherwise
     */
    boolean isColorEqual(Color color);

    boolean isAvailable(Corner corner, boolean isRetro);
}
