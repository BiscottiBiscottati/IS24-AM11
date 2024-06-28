package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.view.client.TUI.states.TuiStates;
import org.jetbrains.annotations.NotNull;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * This class will read the stdin and then use an argParser to extract the arguments from the
 * received command. The list of arguments will be passed to current TuiState that is saved in the
 * TUIUpdater. TuiState is an interface that requires the implementation of the method
 * passArgs(Actuator actuator, List<String> positionalArgs). The Actuator is the class that will
 * call the methods of the CltToNetConnector that will then communicate with the server.
 */


public class Reader {
    private static final Pattern SPACE_SPLIT = Pattern.compile("\\s+");
    private final @NotNull Scanner input;
    private final @NotNull TuiUpdater tuiUpdater;
    private final @NotNull Actuator actuator;

    public Reader() {
        this.input = new Scanner(System.in);
        this.tuiUpdater = new TuiUpdater(TuiStates.CONNECTING);
        this.actuator = new Actuator(tuiUpdater);
    }

    /**
     * Reads the input from the user and passes it to the current TUI state
     */
    public void listen() {
        String string = input.nextLine().strip();
        tuiUpdater.getCurrentTuiState().passArgs(actuator, SPACE_SPLIT.split(string));
    }

}



