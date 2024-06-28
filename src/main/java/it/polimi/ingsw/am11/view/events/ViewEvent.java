package it.polimi.ingsw.am11.view.events;

import it.polimi.ingsw.am11.view.events.utils.ActionMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is a superclass for the events that are sent to the view through the PlayerViewUpdater
 * or the TableViewUpdater. It contains the old and new value of the event, and the action that has
 * been performed.
 */
public abstract class ViewEvent {
    /**
     * This method returns the type of action that has occurred.
     *
     * @return the action that has been performed.
     */
    public @NotNull ActionMode getAction() {
        if (this.getOldValue() == null && this.getNewValue() == null) return ActionMode.CLEAR;
        else if (this.getOldValue() == null) return ActionMode.INSERTION;
        else if (this.getNewValue() == null) return ActionMode.REMOVAL;
        else return ActionMode.CLEAR;
    }

    /**
     * This method returns the old value of the information change that fired the event.
     *
     * @return the old value of the information change that fired the event.
     */
    public abstract @Nullable Object getOldValue();

    /**
     * This method returns the new value of the information change that fired the event.
     *
     * @return the new value of the information change that fired the event.
     */
    public abstract @Nullable Object getNewValue();

    /**
     * This method is guaranteed something not null that represents the event
     *
     * @return the value of the action that has been performed.
     */
    public abstract @NotNull Object getValueOfAction();
}
