package it.polimi.ingsw.am11.view.client.TUI.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsUtils.class);

    private ConsUtils() {
    }

    public static void clear() {
        // Check the operating system
        if (System.getProperty("os.name").startsWith("Windows")) {
            try {
                // Enable ANSI escape codes on Windows
                new ProcessBuilder("cmd", "/c", "echo \033[H\033[2J").inheritIO().start().waitFor();
            } catch (Exception e) {
                LOGGER.error("Error while clearing the console", e);
            }
        } else {
            // Unix-based systems
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }


}
