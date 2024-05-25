package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.Nullable;

public class Placing extends TUIState {

    public Placing(MiniGameModel model) {
        super(model);
    }

    @Override
    public void passArgs(Actuator actuator, String[] args) {
//        String word = positionalArgs.getFirst();
//
//        try {
//            switch (word.toLowerCase()) {
//                case "place" -> actuator.place(positionalArgs);
//                case "help" -> help();
//                case "exit" -> Actuator.close();
//                default -> specificHelp();
//            }
//        } catch (InvalidArgumetsException e) {
//            //TODO
//        }
    }

    @Override
    public void restart(boolean dueToEx, @Nullable Exception exception) {

    }

}
