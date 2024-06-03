package it.polimi.ingsw.am11.persistence.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;

public class DirectorCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DirectorCreator.class);

    private DirectorCreator() {
    }

    static void createGameDirectories() {
        try {
            Files.createDirectories(Constants.GAME_DIR);
            Files.createDirectories(Constants.SAVES_DIR);
        } catch (IOException e) {
            LOGGER.error("Error creating game directories: {}", e.getMessage());
        }
    }
}
