package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.TUI.exceptions.InvalidArgumetsException;

import java.net.UnknownHostException;
import java.util.List;

public class Connecting implements TUIState {

    boolean connecting = false;

    @Override
    public void passArgs(Actuator actuator, List<String> positionalArgs) {
        String word = positionalArgs.getFirst();
        try {
            switch (word.toLowerCase()) {
                case "connecting" -> {
                    if (! connecting) {
                        actuator.connect(positionalArgs);
                        connecting = true;
                    } else {
                        alreadyConnecting();
                    }
                }
                case "help" -> help();
                case "exit" -> Actuator.close();
            }
        } catch (InvalidArgumetsException ex) {
            connecting = false;
            //TODO
            System.out.println("Invalid arguments");
            specificHelp();
        } catch (UnknownHostException ex) {
            connecting = false;
            //TODO
            System.out.println("Wrong ip");
            specificHelp();
        }

    }

    private void alreadyConnecting() {

    }

    private void help() {

    }

    private void specificHelp() {

    }

}
