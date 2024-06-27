package it.polimi.ingsw.am11.model.utils.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am11.model.utils.memento.GameModelMemento;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class SavesManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SavesManager.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Path DB_PATH = DirectoryCreator.SAVES_DIR.resolve("saves.sqlite");
    public static final String CONNECTION_URL = "jdbc:sqlite:" + DB_PATH;

    public static void saveGame(@NotNull GameModelMemento modelMemento) {
        createDB();

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             Statement statement = conn.createStatement()) {

            createTables(statement);

            String saveTime = LocalDateTime.now(ZoneId.of("Europe/Rome"))
                                           .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String saveData = MAPPER.writeValueAsString(modelMemento);

            try (PreparedStatement insertStm = conn.prepareStatement(SQLQuery.INSERT_SAVE)) {
                insertStm.setString(1, saveTime);
                insertStm.setString(2, saveData);
                insertStm.executeUpdate();
            }

        } catch (SQLException | JsonProcessingException e) {
            LOGGER.error("Error saving game: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void createDB() {
        if (Files.notExists(DB_PATH)) {
            try {
                DirectoryCreator.createGameDirectories();
                Files.createFile(DB_PATH);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void createTables(@NotNull Statement statement) throws SQLException {
        statement.addBatch(SQLQuery.CREATE_SAVES_TABLE);
        statement.addBatch(SQLQuery.CREATE_CUSTOM_TABLE);
        statement.addBatch(SQLQuery.TRIGGER_TO_DELETE_OLDER);
        statement.executeBatch();
    }

    public static @NotNull Optional<GameModelMemento> loadMostRecentGame() {
        if (Files.notExists(DB_PATH)) {
            return Optional.empty();
        }

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(SQLQuery.SELECT_MOST_RECENT)) {

            if (! resultSet.next()) {
                return Optional.empty();
            }

            String data = resultSet.getString("save_data");

            GameModelMemento modelMemento = MAPPER.readValue(data, new TypeReference<>() {});
            return Optional.of(modelMemento);

        } catch (SQLException | IOException e) {
            LOGGER.error("Error loading game: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void deleteAll() {
        if (Files.notExists(DB_PATH)) return;

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             Statement statement = conn.createStatement()) {
            LOGGER.debug("Deleting all saves");
            statement.addBatch(SQLQuery.DELETE_ALL_SAVES);
            statement.addBatch(SQLQuery.DELETE_ALL_CUSTOM);
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
