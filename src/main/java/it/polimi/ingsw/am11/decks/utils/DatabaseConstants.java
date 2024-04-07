package it.polimi.ingsw.am11.decks.utils;

/**
 * Constants for the database such as the URL.
 */
public class DatabaseConstants {
    /**
     * The URL of the database.
     * <p>
     * Uses the <code>getResource</code> method to locate
     * the path of "/db/cards.sqlite" in the resources' folder.
     */
    public static final String DATABASE_URL =
            "jdbc:sqlite:" + DatabaseConstants.class.getResource("/db/cards.sqlite");
}
