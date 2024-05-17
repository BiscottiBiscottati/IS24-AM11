package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.TUI.exceptions.InvalidArgumetsException;

import java.util.List;

public class Placing implements TUIState {
    @Override
    public void passArgs(Actuator actuator, List<String> positionalArgs) {
        String word = positionalArgs.getFirst();

        try {
            switch (word.toLowerCase()) {
                case "place" -> actuator.place(positionalArgs);
                case "help" -> help();
                case "exit" -> Actuator.close();
                default -> specificHelp();
            }
        } catch (InvalidArgumetsException e) {
            //TODO
        }
    }

    private void help() {

    }

    private void specificHelp() {

    }
}
