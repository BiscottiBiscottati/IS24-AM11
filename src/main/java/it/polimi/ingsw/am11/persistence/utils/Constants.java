package it.polimi.ingsw.am11.persistence.utils;

import java.nio.file.Path;

public class Constants {
    public static final Path GAME_DIR = Path.of(System.getProperty("user.home"),
                                                "CodexNaturalis");
    public static final Path SAVES_DIR = GAME_DIR.resolve("saves");
}
