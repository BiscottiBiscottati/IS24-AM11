package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.Nullable;

public class Drawing extends TUIState {
    private MiniGameModel model;

    public Drawing(MiniGameModel model) {
        super(model);
    }

    @Override
    public void passArgs(Actuator actuator, String[] args) {
        //TODO

    }

    @Override
    public void restart(boolean dueToEx, @Nullable Exception exception) {

    }
}
