package it.polimi.ingsw.am11.model.cards.utils;

import it.polimi.ingsw.am11.model.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.model.cards.starter.StarterCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;

import java.util.Set;

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

    CornerContainer getItemCorner(Corner corner, boolean isRetro);

    Set<Color> getCenter(boolean isRetro);
}
