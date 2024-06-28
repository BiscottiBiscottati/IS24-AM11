package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This abstract class represents the states of the TUI. The states are used to display specific
 * information to the user, each state will accept only specific commands and will display only
 * specific information.
 */
public abstract class TUIState {

    protected final MiniGameModel model;


    protected TUIState(@NotNull MiniGameModel model) {
        this.model = model;
    }

    /**
     * This method is used to pass the read line to the TUI state
     *
     * @param actuator the actuator that will call the methods of the CltToNetConnector
     * @param args     the arguments passed by the user
     */
    public abstract void passArgs(Actuator actuator, String[] args);

    /**
     * This method is used to restart the TUI state
     *
     * @param dueToEx   if the restart is due to an exception
     * @param exception the exception that caused the restart if present
     */
    public abstract void restart(boolean dueToEx, @Nullable Exception exception);

    /**
     * This method is used to identify the specific state of an instance of a class extending
     * TUIState.
     *
     * @return the specific TuiStates enum value representing the state of the instance.
     */
    public abstract TuiStates getState();
}

