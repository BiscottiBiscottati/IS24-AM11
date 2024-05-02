package it.polimi.ingsw.am11.view.events;

import it.polimi.ingsw.am11.view.events.utils.ActionMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ViewEvent {
    public ActionMode getAction() {
        if (this.getOldValue() == null && this.getNewValue() == null) return ActionMode.CLEAR;
        else if (this.getOldValue() == null) return ActionMode.INSERTION;
        else if (this.getNewValue() == null) return ActionMode.REMOVAL;
        else return ActionMode.CLEAR;
    }

    public abstract @Nullable Object getOldValue();

    public abstract @Nullable Object getNewValue();

    public abstract @NotNull Object getValueOfAction();
}
