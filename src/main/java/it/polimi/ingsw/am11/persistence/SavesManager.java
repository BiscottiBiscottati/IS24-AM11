package it.polimi.ingsw.am11.persistence;

import it.polimi.ingsw.am11.persistence.memento.GameModelMemento;
import it.polimi.ingsw.am11.persistence.utils.DirectoryCreator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class SavesManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SavesManager.class);

    private static final Path DB_PATH = DirectoryCreator.SAVES_DIR.resolve("saves.sqlite");
    private static final String CONNECTION_URL = "jdbc:sqlite:" + DB_PATH;

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS saves (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                save_time TEXT NOT NULL,
                save_data BLOB NOT NULL
            );
            CREATE TABLE IF NOT EXISTS custom_saves (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                save_name TEXT NOT NULL DEFAULT 'no_name',
                save_time TEXT NOT NULL,
                save_data BLOB NOT NULL
            );
            """;

    private static final String INSERT_SAVE = """
            INSERT INTO saves (save_time, save_data)
            VALUES (?, ?);
            """;

    private static final String SELECT_MOST_RECENT = """
            SELECT save_data
            FROM saves
            ORDER BY save_time DESC
            LIMIT 1;
            """;

    public static void saveGame(@NotNull GameModelMemento modelMemento) {
        if (Files.notExists(DB_PATH)) {
            try {
                DirectoryCreator.createGameDirectories();
                Files.createFile(DB_PATH);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             Statement statement = conn.createStatement()) {
            statement.execute(CREATE_TABLE);

            String saveTime =
                    LocalDateTime.now(ZoneId.of("Europe/Rome"))
                                 .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            try (PreparedStatement insertStm = conn.prepareStatement(INSERT_SAVE)) {
                insertStm.setString(1, saveTime);

                insertStm.setBytes(2, toBytes(modelMemento));

                insertStm.executeUpdate();
            }

        } catch (SQLException e) {
            LOGGER.error("Error saving game: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Contract("_ -> new")
    private static byte @NotNull [] toBytes(@NotNull GameModelMemento modelMemento) {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(byteOut)) {
            out.writeObject(modelMemento);
            out.flush();
            return byteOut.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<GameModelMemento> loadMostRecentGame() {
        if (Files.notExists(DB_PATH)) {
            return Optional.empty();
        }

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_MOST_RECENT)) {

            if (! resultSet.next()) {
                return Optional.empty();
            }

            byte[] data = resultSet.getBytes("save_data");

            try (ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
                 ObjectInputStream objIn = new ObjectInputStream(byteIn)) {
                return Optional.of((GameModelMemento) objIn.readObject());
            }

        } catch (SQLException | IOException | ClassNotFoundException e) {
            LOGGER.error("Error loading game: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
