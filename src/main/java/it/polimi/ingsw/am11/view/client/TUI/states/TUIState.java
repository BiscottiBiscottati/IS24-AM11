package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import org.jetbrains.annotations.Nullable;

public interface TUIState {
    void passArgs(Actuator actuator, String[] args);

    void restart(boolean dueToEx, @Nullable Exception exception);
}

