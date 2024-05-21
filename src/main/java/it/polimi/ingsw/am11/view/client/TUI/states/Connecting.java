package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.utils.Constants;
import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.TUI.utils.ConsUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.regex.Pattern;

public class Connecting implements TUIState {

    private static final String chooseSocketOrRmi = "Choose socket or rmi >>> \033[K";
    private static final String chooseIp = "Enter IP, leave empty for default >>> \033[K";
    private static final String choosePort = "Enter Port, leave empty for default >>> \033[K";
    private static final String setSocketOrRmi = "Type of connection set to: ";
    private static final String setIp = "IP address set to: ";
    private static final String setPort = "Port set to: ";

    private boolean alreadyError = false;
    private String type = "";
    private String ip = "";
    private int port = - 1;
    private int count = 0;

    @Override
    public void passArgs(Actuator actuator, String[] args) {
        ArgParser parser = setUpOptions();

        // Handel empty string
        if (args[0].isEmpty()) {
            if (type.isEmpty()) {
                System.out.print("\033[F" + chooseSocketOrRmi);
                return;
            } else if (ip.isEmpty()) {
                ip = "localhost";
                upClearDownThenFalse();
                System.out.println(setIp + ip);
                if (port == - 1) {
                    System.out.print(choosePort);
                    return;
                }
            } else if (port == - 1) {
                switch (type) {
                    case "socket" -> {
                        port = Constants.DEFAULT_SOCKET_PORT;
                        upClearDownThenFalse();
                        System.out.println(setPort + Constants.DEFAULT_SOCKET_PORT);
                    }
                    case "rmi" -> {
                        port = Constants.DEFAULT_RMI_PORT;
                        upClearDownThenFalse();
                        System.out.println(setPort + Constants.DEFAULT_RMI_PORT);
                    }
                    default ->
                            throw new RuntimeException("type is set neither to rmi nor to socket");
                }
                actuator.connect(type, ip, port);
                return;
            }

        }


        // Handle exception
        try {
            parser.parse(args);
            if (parser.getPositionalArgs().size() > 1) {
                throw new ParsingErrorException(
                        "Too many arguments, from: " + parser.getPositionalArgs().get(1));
            }
        } catch (ParsingErrorException e) {
            errorsHappensEvenTwice("ERROR: " + e.getMessage());
            if (type.isEmpty()) {
                System.out.print(chooseSocketOrRmi);
                alreadyError = true;
            } else if (ip.isEmpty()) {
                System.out.print(chooseIp);
                alreadyError = true;
            } else if (port == - 1) {
                System.out.print(choosePort);
                alreadyError = true;
            }
        }

        String word = parser.getPositionalArgs().getFirst();
        switch (word.toLowerCase()) {
            case "help" -> Actuator.help();
            //Maybe a return is needed after help
            case "exit" -> Actuator.close();
        }

        // Handling of Choose socket or rmi >>>
        if (type.isEmpty()) {
            // Check type
            switch (word.toLowerCase()) {
                case "socket" -> type = "socket";
                case "rmi" -> type = "rmi";
                default -> {
                    errorsHappensEvenTwice("ERROR: " + word + " is not a valid argument");
                    System.out.print(chooseSocketOrRmi);
                    alreadyError = true;
                }
            }

            //check if default
            if (Optional.ofNullable(
                    parser.getOption("default").orElseThrow().getValue()).isPresent()) {
                ip = "localhost";
                switch (type) {
                    case "socket" -> port = Constants.DEFAULT_SOCKET_PORT;
                    case "rmi" -> port = Constants.DEFAULT_RMI_PORT;
                }
            }

            //check ip, if given
            String optIP = parser.getOption("ip").orElseThrow().getValue();
            if (! optIP.isEmpty()) {
                if (validateIP(optIP) || optIP.equals("localhost")) {
                    ip = optIP;
                } else {
                    errorsHappensEvenTwice("ERROR: " + optIP + " is not a valid value for IP");
                    System.out.print(chooseSocketOrRmi);
                    alreadyError = true;
                }
            }

            //check port if given
            String optPort = parser.getOption("port").orElseThrow().getValue();
            if (! optPort.isEmpty()) {
                try {
                    port = Integer.parseInt(optPort);
                } catch (NumberFormatException e) {
                    errorsHappensEvenTwice("ERROR: " + optPort + " is not a valid value for port");
                    System.out.print(chooseSocketOrRmi);
                    alreadyError = true;
                }
            }

            //ask for new value if necessary
            if (ip.isEmpty()) {
                upClearDownThenFalse();
                System.out.println(setSocketOrRmi + type);
                if (port != - 1) {
                    System.out.println(setPort + port);
                }
                System.out.print(chooseIp);
                return;
            }
            if (port == - 1) {
                upClearDownThenFalse();
                System.out.println(setSocketOrRmi + type);
                System.out.println(setIp + ip);
                System.out.print(choosePort);
                return;
            }
        }
        // Handling of Enter IP, leave empty for default >>>
        else if (ip.isEmpty()) {
            if (validateIP(word) || word.equals("localhost")) {
                ip = word;
            } else {
                errorsHappensEvenTwice("ERROR: " + word + " is not a valid value for IP");
                System.out.print(chooseIp);
                alreadyError = true;
                return;
            }

            upClearDownThenFalse();
            System.out.println(setIp + ip);
            if (port == - 1) {
                System.out.print(choosePort);
                return;
            }
        }
        // Handling of Enter Port, leave empty for default >>>
        else if (port == - 1) {
            try {
                port = Integer.parseInt(word);
            } catch (NumberFormatException e) {
                errorsHappensEvenTwice("ERROR: " + word + " is not a valid value for port");
                System.out.print(choosePort);
                alreadyError = true;
            }

            upClearDownThenFalse();
            System.out.println(setPort + port);
        }


        actuator.connect(type, ip, port);


    }

    private static @NotNull ArgParser setUpOptions() {
        ArgParser argParser = new ArgParser();
        argParser.addOption("default",
                            "Use default IP and Port for that connection",
                            false);
        argParser.addOption("port",
                            "Server port to connect to",
                            "");
        argParser.addOption("ip",
                            "IP address to connect to",
                            "");
        return argParser;
    }

    private void upClearDownThenFalse() {
        if (alreadyError) {
            System.out.print("\033[F" + "\033[K");
            System.out.print("\033[2F" + "\033[K");
            alreadyError = false;
        } else {
            System.out.print("\033[F" + "\033[K");
        }
    }

    private void errorsHappensEvenTwice(String text) {
        if (alreadyError) {
            System.out.print("\033[F" + "\033[K");
        }
        System.out.println("\033[F" + "\033[K" + text);
    }

    private static boolean validateIP(String ip) {
        // Regex expression for validating IPv4
        String regexV4 = "(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\.){3}" +
                         "([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])";

        // Regex expression for validating IPv6
        String regexV6 = "((([0-9a-fA-F]){1,4})\\:){7}([0-9a-fA-F]){1,4}";

        Pattern pV4 = Pattern.compile(regexV4);
        Pattern pV6 = Pattern.compile(regexV6);

        // Checking if it is a valid IPv4 addresses
        if (pV4.matcher(ip).matches())
            return true;

            // Checking if it is a valid IPv6 addresses
        else return pV6.matcher(ip).matches();

    }

    @Override
    public void restart(boolean dueToEx, Exception exception) {
        count++;
        alreadyError = false;
        type = "";
        ip = "";
        port = - 1;

        ConsUtils.clear();
        System.out.println("""
                                    ++++++++++++++++++++++++++++
                                                                      \s
                                     STATUS: Connecting to the server...
                                                                      \s
                                    ++++++++++++++++++++++++++++
                                   \s""");

        if (dueToEx) {
            System.out.println("ERROR DURING CONNECTION: " + exception.getMessage());
            alreadyError = true;
        }
        System.out.println("To join a new game you need to connect to the server, enter the " +
                           "information" + count + ":");

        System.out.print(chooseSocketOrRmi);
    }
}

