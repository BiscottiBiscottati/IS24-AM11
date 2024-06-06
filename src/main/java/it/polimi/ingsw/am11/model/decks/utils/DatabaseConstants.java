package it.polimi.ingsw.am11.model.decks.utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class contains constants that are used for database operations.
 */
public class DatabaseConstants {
    /**
     * The URL of the database.
     * <p>
     * Uses the <code>getResource</code> method to locate the path of "/db/cards.sqlite" in the
     * resources' folder.
     */
    public static final @NotNull String DATABASE_URL;

    static {
        File tempDbFile;
        try (InputStream inputStream = DatabaseConstants.class.getResourceAsStream(
                "/db/cards.sqlite")) {

            // check if the database file is found
            if (inputStream == null) {
                throw new RuntimeException("Database file not found");
            }

            // create a temporary file to store the database
            tempDbFile = File.createTempFile("it/polimi/ingsw/am11/view/client/GUI/window/cards",
                                             ".sqlite");

            // copy the database data to the temporary file
            try (FileOutputStream outputStream = new FileOutputStream(tempDbFile)) {
                inputStream.transferTo(outputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DATABASE_URL = "jdbc:sqlite:" + tempDbFile.getAbsolutePath();
    }
}
