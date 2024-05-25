package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.Nullable;

public class Ended extends TUIState {

    public Ended(MiniGameModel model) {
        super(model);
    }

    @Override
    public void passArgs(Actuator actuator, String[] args) {
        //TODO

//        String word = positionalArgs.getFirst();
//
//        switch (word.toLowerCase()) {
//            case "help" -> help();
//            case "exit" -> Actuator.close();
//            default -> specificHelp();
//        }

    }

    @Override
    public void restart(boolean dueToEx, @Nullable Exception exception) {

    }

}
