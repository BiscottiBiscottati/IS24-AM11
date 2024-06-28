package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.utils.Constants;
import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.TUI.printers.InfoBarPrinter;
import it.polimi.ingsw.am11.view.client.TUI.utils.ConsUtils;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.regex.Pattern;

public class Connecting extends TUIState {

    static final String chooseSocketOrRmi = "Choose socket or rmi >>> \033[K";
    static final String chooseIp = "Enter IP, leave empty for default >>> \033[K";
    static final String choosePort = "Enter Port, leave empty for default >>> \033[K";
    private static final String setSocketOrRmi = "Type of connection set to: ";
    private static final String setIp = "IP address set to: ";
    private static final String setPort = "Port set to: ";
    private static final String infoBar = " STATUS: Connecting to the server...";
    @NotNull
    String type = "";
    @NotNull
    String ip = "";
    int port = - 1;
    private boolean alreadyError = false;


    public Connecting(@NotNull MiniGameModel model) {
        super(model);
    }

    @Override
    public void passArgs(@NotNull Actuator actuator, String @NotNull [] args) {
        ArgParser parser = setUpOptions();

        // Handel empty string
        if (args[0].isEmpty()) {
            if (type.isEmpty()) {
                System.out.print("\033[F");
                TuiStates.printAskLine(this);
                return;
            } else if (ip.isEmpty()) {
                ip = "localhost";
                upClearDownThenFalse();
                System.out.println(setIp + ip);
                TuiStates.printAskLine(this);
                return;
            } else if (port == - 1) {
                switch (type) {
                    case "socket" -> {
                        port = Constants.DEFAULT_SOCKET_PORT;
                        upClearDownThenFalse();
                        System.out.println(setPort + port);
                    }
                    case "rmi" -> {
                        port = Constants.DEFAULT_RMI_PORT;
                        upClearDownThenFalse();
                        System.out.println(setPort + port);
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
            TuiStates.printAskLine(this);
            alreadyError = true;

            return;
        }

        String word = parser.getPositionalArgs().getFirst();
        switch (word.toLowerCase()) {
            case "help" -> {
                Actuator.help();
                return;
            }
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
                    TuiStates.printAskLine(this);
                    alreadyError = true;
                    return;
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
            assert optIP != null;
            if (! optIP.isEmpty()) {
                if (validateIP(optIP) || optIP.equals("localhost")) {
                    ip = optIP;
                } else {
                    errorsHappensEvenTwice("ERROR: " + optIP + " is not a valid value for IP");
                    TuiStates.printAskLine(this);
                    alreadyError = true;
                }
            }

            //check port if given
            String optPort = parser.getOption("port").orElseThrow().getValue();
            assert optPort != null;
            if (! optPort.isEmpty()) {
                try {
                    port = Integer.parseInt(optPort);
                } catch (NumberFormatException e) {
                    errorsHappensEvenTwice("ERROR: " + optPort + " is not a valid value for port");
                    TuiStates.printAskLine(this);
                    alreadyError = true;
                }
            }

            //ask for a new value if necessary
            if (ip.isEmpty()) {
                upClearDownThenFalse();
                System.out.println(setSocketOrRmi + type);
                if (port != - 1) {
                    System.out.println(setPort + port);
                }
                TuiStates.printAskLine(this);
                return;
            }
            if (port == - 1) {
                upClearDownThenFalse();
                System.out.println(setSocketOrRmi + type);
                System.out.println(setIp + ip);
                TuiStates.printAskLine(this);
                return;
            }
        }
        // Handling of Enter IP, leave empty for default >>>
        else if (ip.isEmpty()) {
            if (validateIP(word) || word.equals("localhost")) {
                ip = word;
            } else {
                errorsHappensEvenTwice("ERROR: " + word + " is not a valid value for IP");
                TuiStates.printAskLine(this);
                alreadyError = true;
                return;
            }

            upClearDownThenFalse();
            System.out.println(setIp + ip);
            if (port == - 1) {
                TuiStates.printAskLine(this);
                return;
            }
        }
        // Handling of Enter Port, leave empty for default >>>
        else if (port == - 1) {
            try {
                port = Integer.parseInt(word);
            } catch (NumberFormatException e) {
                errorsHappensEvenTwice("ERROR: " + word + " is not a valid value for port");
                TuiStates.printAskLine(this);
                alreadyError = true;
                return;
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
            System.out.print("\033[F" + "\033[K");
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

    private static boolean validateIP(@NotNull String ip) {
        // Regex expression for validating IPv4
        String regexV4 = "(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\.){3}" +
                         "([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])";

        // Regex expression for validating IPv6
        String regexV6 = "((([0-9a-fA-F]){1,4})\\:){7}([0-9a-fA-F]){1,4}";

        Pattern pV4 = Pattern.compile(regexV4);
        Pattern pV6 = Pattern.compile(regexV6);

        // Checking if it is a valid IPv4 address
        if (pV4.matcher(ip).matches())
            return true;

            // Checking if it is a valid IPv6 address
        else return pV6.matcher(ip).matches();

    }

    @Override
    public void restart(boolean dueToEx, @Nullable Exception exception) {
        alreadyError = false;
        type = "";
        ip = "";
        port = - 1;

        ConsUtils.clear();
        InfoBarPrinter.printInfoBar(infoBar);

        if (dueToEx) {
            System.out.println("ERROR DURING CONNECTION: " + exception.getMessage());
        }
        System.out.println("To join a new game you need to connect to the server, enter the " +
                           "information: ");

        TuiStates.printAskLine(this);
    }

    @Override
    public @NotNull TuiStates getState() {
        return TuiStates.CONNECTING;
    }
}

