package it.polimi.ingsw.am11.view;

import it.polimi.ingsw.am11.view.events.FieldChangeEvent;
import it.polimi.ingsw.am11.view.events.HandChangeEvent;
import it.polimi.ingsw.am11.view.events.TurnChangeEvent;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ViewUpdater implements PropertyChangeListener {

    private final VirtualView virtualView;

    public ViewUpdater(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    @Override
    public void propertyChange(@NotNull PropertyChangeEvent evt) {
        // TODO missing logic to connect to the view
        switch (evt) {
            case HandChangeEvent handEvt -> System.out.println("HandChangeEvent");
            case TurnChangeEvent turnEvt -> System.out.println("TurnChangeEvent");
            case FieldChangeEvent fieldEvt -> System.out.println("FieldChangeEvent");
            default -> throw new IllegalArgumentException("Unexpected value: " + evt);
        }
    }
}
