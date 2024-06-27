package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;

/**
 * Functional interface used to create a new state.
 */
@FunctionalInterface
public interface StateFactory {
    /**
     * Create a new state.
     *
     * @param model The new state needs a reference to the model
     * @return The new state
     */
    @NotNull
    TUIState createState(@NotNull MiniGameModel model);
}
