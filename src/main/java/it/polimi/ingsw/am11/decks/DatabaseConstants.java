package it.polimi.ingsw.am11.decks;

public class DatabaseConstants {
    public static final String DATABASE_URL =
            "jdbc:sqlite::memory:" + DatabaseConstants.class.getResource("/db/cards.sqlite");
}
