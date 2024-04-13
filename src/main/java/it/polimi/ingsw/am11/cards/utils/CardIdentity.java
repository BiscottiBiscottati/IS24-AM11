package it.polimi.ingsw.am11.cards.utils;

/**
 * A common interface for all cards that can be identified by a unique integer id.
 * <p>
 * Each card must have a unique id between all the cards in the game. This is not ensured by the interface, but by the
 * SQLite database.
 */
public interface CardIdentity {
    int getId();
}
