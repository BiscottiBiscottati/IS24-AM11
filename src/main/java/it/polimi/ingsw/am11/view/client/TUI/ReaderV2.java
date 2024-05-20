package it.polimi.ingsw.am11.view.client.TUI;

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

    public ReaderV2(TuiUpdater tuiUpdater) {
        this.input = new Scanner(System.in);
        this.tuiUpdater = tuiUpdater;
        this.actuator = new Actuator(tuiUpdater);
    }

    public void listen() {
        String string = input.nextLine().strip();
//        if (string.isEmpty()) {
//            System.out.print("\033[A");
//            return;
//        }
        tuiUpdater.getCurrentTuiState().passArgs(actuator, string.split("\\s+"));
    }

}



