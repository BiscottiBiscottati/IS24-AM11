package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;

import java.util.ArrayList;
import java.util.Scanner;

// This class will read the stdin and then use an argParser to extract the arguments from the
// received command. The list of arguments will be passed to current TuiState that is saved in
// the TUIUpdater. TuiState is an interface that requires the implementation of the method
// passArgs(Actuator actuator, List<String> positionalArgs).
// The Actuator is the class that will call the methods of the CltToNetConnector that will then
// communicate with the server.

public class ReaderV2 {
    private final Scanner input;
    private final TuiUpdater tuiUpdater;
    private final Actuator actuator;


    public ReaderV2(MiniGameModel model, TuiUpdater tuiUpdater) {
        this.input = new Scanner(System.in);
        this.tuiUpdater = tuiUpdater;
        this.actuator = new Actuator(tuiUpdater);
    }

    public void listen() {
        ArgParser parser = setUpOptions();
        try {
            parser.parse(input.nextLine().strip().split("\\s+"));
        } catch (ParsingErrorException e) {
            System.out.println(e.getMessage());
        }
        tuiUpdater.getTuiState().passArgs(actuator, new ArrayList<>(parser.getPositionalArgs()));
    }

    private static ArgParser setUpOptions() {
        ArgParser argParser = new ArgParser();
//        argParser.addOption("rmi",
//                            "RMI port to use for the server",
//                            String.valueOf(Constants.DEFAULT_RMI_PORT));

        return argParser;
    }
}



