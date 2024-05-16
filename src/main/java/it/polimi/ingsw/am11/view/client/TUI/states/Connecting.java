package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.TUI.exceptions.InvalidArgumetsException;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

public class Connecting implements TUIState {


    @Override
    public void passArgs(Actuator actuator, List<String> positionalArgs) {
        String word = positionalArgs.getFirst();
        try {
            switch (word.toLowerCase()) {
                case "connect" -> actuator.connect(positionalArgs);
                case "help" -> help();
                case "exit" -> Actuator.close();
                default -> {
                    System.out.println("Wrong input 0");
                    for (String st : positionalArgs) {
                        System.out.println(">" + st);
                    }
                }
            }
        } catch (InvalidArgumetsException ex) {
            //TODO
            System.out.println("Invalid arguments");
            specificHelp();
        } catch (UnknownHostException ex) {

            //TODO
            System.out.println("Wrong ip");
            specificHelp();
        } catch (IOException ex) {

            //TODO
            System.out.println(
                    "Of course, you couldn't even manage to connect to the server. But then " +
                    "again, I'm not surprised. With your track record, I didn't expect anything " +
                    "different.");

        }

    }

    private void help() {

    }

    private void specificHelp() {
        System.out.println("Wrong input 1");
    }

}
