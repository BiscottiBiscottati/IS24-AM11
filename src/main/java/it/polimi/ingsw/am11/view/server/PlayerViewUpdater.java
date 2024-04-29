package it.polimi.ingsw.am11.view.server;

import it.polimi.ingsw.am11.view.events.HandChangeEvent;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PlayerViewUpdater implements PropertyChangeListener {

    private final VirtualPlayerView virtualView;

    public PlayerViewUpdater(VirtualPlayerView virtualView) {
        this.virtualView = virtualView;
    }

    @Override
    public void propertyChange(@NotNull PropertyChangeEvent evt) {
        // TODO missing logic to connect to the view
        switch (evt) {
            case HandChangeEvent handEvt -> System.out.println("HandChangeEvent");
            default -> throw new IllegalArgumentException("Unexpected value: " + evt);
        }
    }
}
