package it.polimi.ingsw.am11.model.utils.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DirectoryCreator {

    public static final Path GAME_DIR = Path.of(System.getProperty("user.home"),
                                                "codex");
    public static final Path SAVES_DIR = GAME_DIR.resolve("saves");

    private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryCreator.class);

    private DirectoryCreator() {
    }

    public static void createGameDirectories() {
        try {
            if (Files.notExists(GAME_DIR)) {
                LOGGER.info("SYSTEM: Creating game directory: {}", GAME_DIR);
                Files.createDirectories(GAME_DIR);
            }
            if (Files.notExists(SAVES_DIR)) {
                LOGGER.info("SYSTEM: Creating saves directory: {}", SAVES_DIR);
                Files.createDirectories(SAVES_DIR);
            }
        } catch (IOException e) {
            LOGGER.error("SYSTEM: Error creating game directories: {}", e.getMessage());
        }
    }
}
