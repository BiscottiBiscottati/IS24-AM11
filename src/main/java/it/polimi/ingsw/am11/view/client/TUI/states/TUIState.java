package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.Nullable;

public abstract class TUIState {

    protected MiniGameModel model;

    protected TUIState(MiniGameModel model) {
        this.model = model;
    }

    public void passArgs(Actuator actuator, String[] args){
    }

    public void restart(boolean dueToEx, @Nullable Exception exception){

    }
}

