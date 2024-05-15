package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;

import java.util.ArrayList;
import java.util.Scanner;

public class ReaderV2 {
    private final Scanner input;
    private final TuiUpdater tuiUpdater;
    private final MiniGameModel model;
    private final ArgParser parser;
    private final Actuator actuator;


    public ReaderV2(MiniGameModel model, TuiUpdater tuiUpdater) {
        this.input = new Scanner(System.in);
        this.model = model;
        this.tuiUpdater = tuiUpdater;
        this.parser = setUpOptions();
        this.actuator = new Actuator(tuiUpdater);
    }

    private static ArgParser setUpOptions() {
        ArgParser argParser = new ArgParser();
//        argParser.addOption("rmi",
//                            "RMI port to use for the server",
//                            String.valueOf(Constants.DEFAULT_RMI_PORT));

        return argParser;
    }

    public void listen() {
        try {
            parser.parse(input.nextLine().strip().split("\\s+"));
        } catch (ParsingErrorException e) {
            System.out.println(e.getMessage());
        }
        tuiUpdater.getTuiState().passArgs(actuator, new ArrayList<>(parser.getPositionalArgs()));
    }
}



