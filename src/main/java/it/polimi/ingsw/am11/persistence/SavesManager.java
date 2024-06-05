package it.polimi.ingsw.am11.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am11.persistence.memento.GameModelMemento;
import it.polimi.ingsw.am11.persistence.utils.DirectoryCreator;
import it.polimi.ingsw.am11.persistence.utils.SQLQuery;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
    private static final String CONNECTION_URL = "jdbc:sqlite:" + DB_PATH;

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
            statement.addBatch(SQLQuery.CREATE_SAVES_TABLE);
            statement.addBatch(SQLQuery.CREATE_CUSTOM_TABLE);
            statement.executeBatch();

            String saveTime = LocalDateTime.now(ZoneId.of("Europe/Rome"))
                                           .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            try (PreparedStatement insertStm = conn.prepareStatement(SQLQuery.INSERT_SAVE)) {
                insertStm.setString(1, saveTime);

                insertStm.setBytes(2, MAPPER.writeValueAsBytes(modelMemento));

                insertStm.executeUpdate();
            }

        } catch (SQLException | JsonProcessingException e) {
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
             ResultSet resultSet = statement.executeQuery(SQLQuery.SELECT_MOST_RECENT)) {

            if (! resultSet.next()) {
                return Optional.empty();
            }

            byte[] data = resultSet.getBytes("save_data");

            return Optional.of(MAPPER.readValue(data, new TypeReference<>() {}));

        } catch (SQLException | IOException e) {
            LOGGER.error("Error loading game: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void deleteAll() {
        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             Statement statement = conn.createStatement()) {
            statement.addBatch(SQLQuery.DELETE_ALL_SAVES);
            statement.addBatch(SQLQuery.DELETE_ALL_CUSTOM);
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
