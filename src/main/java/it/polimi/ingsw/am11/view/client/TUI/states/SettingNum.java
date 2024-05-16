package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.TUI.exceptions.InvalidArgumetsException;

import java.util.List;

public class SettingNum implements TUIState {

    @Override
    public void passArgs(Actuator actuator, List<String> positionalArgs) {
        String word = positionalArgs.getFirst();
        try {
            switch (word) {
                case "setnumofplayers" -> actuator.setNumOfPlayers(positionalArgs);
                case "exit" -> Actuator.close();
                case "help" -> help();
                default -> specificHelp();
            }
        } catch (InvalidArgumetsException ex) {
            //TODO
            System.out.println(
                    "Get it together already! Setting the number of players is basic. Just " +
                    "because you're the de facto moderator doesn't mean you actually deserve it. " +
                    "Shape up or step aside for someone who can handle it.");
        }
    }

    private void help() {

    }

    private void specificHelp() {

    }
}
