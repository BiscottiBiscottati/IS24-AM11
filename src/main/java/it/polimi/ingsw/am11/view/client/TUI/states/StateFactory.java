package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface StateFactory {
    @NotNull
    TUIState createState(@NotNull MiniGameModel model);
}
