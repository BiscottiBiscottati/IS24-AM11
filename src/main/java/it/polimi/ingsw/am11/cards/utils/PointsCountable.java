package it.polimi.ingsw.am11.cards.utils;

import it.polimi.ingsw.am11.players.PlayerField;

/**
 * Interface for cards that can be counted for points.
 */
public interface PointsCountable {

    /**
     * Counts the points obtained from placing the card on the field.
     *
     * @param playerField the player field to count the points from
     * @return the points obtained from placing the card on the field
     */
    int countPoints(PlayerField playerField);
}
