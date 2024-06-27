package it.polimi.ingsw.am11;

import ch.qos.logback.classic.Level;
import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.utils.Constants;
import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import org.fusesource.jansi.AnsiConsole;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Optional;

public class Main {

    public static void main(String @Nullable [] args) throws IOException {

        final String HELP_MESSAGE = """
                Usage: java -jar <jar file> <server|client> [options]
                Options:
                  -rmi <port>    RMI port to use for the server
                  -socket <port> Socket port to use for the server
                  -mode <mode>   Mode to start the application in (gui|tui) (only for client)
                  -v             Print debug logging information
                  -vv            Print trace logging information
                  -resume        Load the most recent save
                """;

        ArgParser parser = setUpOptions();

        AnsiConsole.systemInstall();

        Runtime.getRuntime().addShutdownHook(new Thread(AnsiConsole::systemUninstall));


        if (args == null || args.length < 1) {
            System.out.println(HELP_MESSAGE);
            return;
        }

        try {
            parser.parse(args);
        } catch (ParsingErrorException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        ch.qos.logback.classic.Logger rootlogger =
                (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(
                        Logger.ROOT_LOGGER_NAME);
        rootlogger.setLevel(Level.INFO);
        Optional.ofNullable(parser.getOption("v").orElseThrow().getValue())
                .ifPresent(vValue -> rootlogger.setLevel(Level.DEBUG));
        Optional.ofNullable(parser.getOption("vv").orElseThrow().getValue())
                .ifPresent(vvValue -> rootlogger.setLevel(Level.TRACE));

        String mode = parser.getPositionalArgs().getFirst();
        switch (mode.toLowerCase()) {
            case "server" -> Server.start(parser);
            case "client" -> Client.start(parser);
            case "help" -> System.out.println(HELP_MESSAGE);
            default -> System.out.println("Invalid mode: " + mode + ". Use 'server' or 'client'.");
        }
    }

    private static @NotNull ArgParser setUpOptions() {
        ArgParser argParser = new ArgParser();
        argParser.addOption("rmi",
                            "RMI port to use for the server",
                            String.valueOf(Constants.DEFAULT_RMI_PORT));
        argParser.addOption("socket",
                            "Socket port to use for the server",
                            String.valueOf(Constants.DEFAULT_SOCKET_PORT));
        argParser.addOption("ui",
                            "Mode to start the application in (gui|tui)",
                            "tui");
        argParser.addOption("v",
                            "Print debug logging information",
                            false);
        argParser.addOption("vv",
                            "Print trace logging information",
                            false);
        argParser.addOption("resume",
                            "Load the most recent save",
                            false);
        return argParser;
    }
}
