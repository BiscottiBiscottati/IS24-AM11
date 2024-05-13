package it.polimi.ingsw.am11;

import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.utils.Constants;
import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import org.jetbrains.annotations.NotNull;

public class Main {

    public static void main(String[] args) throws ParsingErrorException {

        final String HELP_MESSAGE = """
                Usage: java -jar <jar file> <server|client> [options]
                Options:
                  -rmi <port>    RMI port to use
                  -socket <port> Socket port to use
                  -mode <mode>   Mode to run the application in (gui|tui) (only for client)
                """;

        ArgParser parser = setUpOptions();

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
                            "RMI port to use",
                            String.valueOf(Constants.DEFAULT_RMI_PORT));
        argParser.addOption("socket",
                            "Socket port to use",
                            String.valueOf(Constants.DEFAULT_SOCKET_PORT));
        argParser.addOption("ui",
                            "Mode to run the application in (gui|tui)",
                            "tui");
        return argParser;
    }
}
