package it.polimi.ingsw.am11;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        final String HELP_MESSAGE = "Usage: java -jar <jar file> <server|client> <port> " +
                                    "[<gui|tui>]";

        if (args == null || args.length < 1) {
            System.out.println(HELP_MESSAGE);
            return;
        }

        String mode = args[0];
        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
        switch (mode.toLowerCase()) {
            case "server" -> Server.start(newArgs);
            case "client" -> Client.start(newArgs);
            case "help" -> System.out.println(HELP_MESSAGE);
            default -> System.out.println("Invalid mode: " + mode + ". Use 'server' or 'client'.");
        }
    }
}
