package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.view.client.TUI.Actuator;

import java.util.List;

public interface TUIState {
    void passArgs(Actuator actuator, List<String> positionalArgs);
}

