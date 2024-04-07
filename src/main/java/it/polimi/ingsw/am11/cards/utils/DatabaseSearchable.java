package it.polimi.ingsw.am11.cards.utils;

/**
 * Interface for objects that can be searched in the database.
 */
public interface DatabaseSearchable {

    /**
     * Gets the name of the column in the database that contains the value to search for.
     *
     * @return the name of the column in the database
     */
    String getColumnName();
}
