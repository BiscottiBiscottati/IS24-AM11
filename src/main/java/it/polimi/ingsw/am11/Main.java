package it.polimi.ingsw.am11;

import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.utils.Constants;
import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import org.fusesource.jansi.AnsiConsole;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Optional;

public class Main {

    public static void main(String[] args) throws IOException {

        final String HELP_MESSAGE = """
                Usage: java -jar <jar file> <server|client> [options]
                Options:
                  -rmi <port>    RMI port to use for the server
                  -socket <port> Socket port to use for the server
                  -mode <mode>   Mode to start the application in (gui|tui) (only for client)
                  -v             Print logging information
                """;

        ArgParser parser = setUpOptions();

        AnsiConsole.systemInstall();


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
        Optional.ofNullable(parser.getOption("v").orElseThrow().getValue())
                .ifPresentOrElse(
                        v -> rootlogger.setLevel(ch.qos.logback.classic.Level.DEBUG),
                        () -> rootlogger.setLevel(ch.qos.logback.classic.Level.INFO));

        String mode = parser.getPositionalArgs().getFirst();
        switch (mode.toLowerCase()) {
            case "server" -> Server.start(parser);
            case "client" -> Client.start(parser);
            case "help" -> System.out.println(HELP_MESSAGE);
            default -> System.out.println("Invalid mode: " + mode + ". Use 'server' or 'client'.");
        }

        AnsiConsole.systemUninstall();
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
                            "Print logging information",
                            false);
        return argParser;
    }
}
