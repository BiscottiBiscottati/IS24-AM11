package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.TUI.exceptions.InvalidArgumetsException;
import it.polimi.ingsw.am11.view.client.TUI.exceptions.TooManyRequestsException;

import java.util.List;

public class SettingName implements TUIState {
    @Override
    public void passArgs(Actuator actuator, List<String> positionalArgs) {
        String word = positionalArgs.getFirst();
        try {
            switch (word.toLowerCase()) {
                case "setnick" -> actuator.setName(positionalArgs);
                case "help" -> help();
                case "exit" -> Actuator.close();
                default -> specificHelp();
            }
        } catch (InvalidArgumetsException ex) {
            //TODO
            System.out.println("Can't you even set your name correctly?");
        } catch (TooManyRequestsException ex) {
            //TODO
            System.out.println("One moment of patience may ward off great disaster. One moment " +
                               "of impatience may ruin a whole life.");
        }
    }

    private void help() {

    }

    private void specificHelp() {

    }
}
