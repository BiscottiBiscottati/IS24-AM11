package it.polimi.ingsw.am11.view.events;

import it.polimi.ingsw.am11.view.utils.ActionMode;

public abstract class ViewEvent {
    public ActionMode getAction() {
        if (this.getOldValue() == null && this.getNewValue() == null) return ActionMode.CLEAR;
        else if (this.getOldValue() == null) return ActionMode.INSERTION;
        else if (this.getNewValue() == null) return ActionMode.REMOVAL;
        else return ActionMode.CLEAR;
    }

    public abstract Object getOldValue();

    public abstract Object getNewValue();
}
