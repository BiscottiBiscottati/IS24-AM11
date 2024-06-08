package it.polimi.ingsw.am11.model.cards.utils;

/**
 * A common interface for all cards that can be identified by a unique integer id.
 * <p>
 * Each card must have a unique id between all the cards in the game. This is not ensured by the
 * interface, but by the SQLite database.
 */
public interface CardIdentity {

    /**
     * This method is part of the CardIdentity interface.
     * <p>
     * It is used to get the unique integer id of the card.
     * <p>
     * Each card must have a unique id between all the cards in the game.
     * <p>
     * This uniqueness is not ensured by the interface, but by the SQLite database.
     *
     * @return int - The unique id of the card.
     */
    int getId();

}
