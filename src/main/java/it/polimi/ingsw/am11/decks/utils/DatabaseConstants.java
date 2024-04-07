package it.polimi.ingsw.am11.decks.utils;

public class DatabaseConstants {
    public static final String DATABASE_URL =
            "jdbc:sqlite:" + DatabaseConstants.class.getResource("/db/cards.sqlite");
}
