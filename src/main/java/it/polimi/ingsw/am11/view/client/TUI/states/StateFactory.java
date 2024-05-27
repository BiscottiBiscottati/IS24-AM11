package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;

@FunctionalInterface
public interface StateFactory {
    TUIState createState(MiniGameModel model);
}
