package it.polimi.ingsw.am11.view.events.view.player;

import it.polimi.ingsw.am11.view.events.PlayerViewEvent;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class CandidateObjectiveEvent extends PlayerViewEvent {

    // TODO to add to model
    private final Set<Integer> oldCardsId;
    private final @NotNull Set<Integer> newCardsId;

    public CandidateObjectiveEvent(@NotNull String player, Set<Integer> oldCardsId,
                                   @NotNull Set<Integer> newCardsId) {
        super(player);
        this.oldCardsId = oldCardsId;
        this.newCardsId = newCardsId;
    }

    @Override
    public void updateView(@NotNull VirtualPlayerView virtualView) {
        virtualView.updatePlayer(this);
    }

    @Override
    public Set<Integer> getOldValue() {
        return oldCardsId;
    }

    @Override
    public @NotNull Set<Integer> getNewValue() {
        return newCardsId;
    }

    @Override
    public @NotNull Set<Integer> getValueOfAction() {
        if (oldCardsId != null) return oldCardsId;
        if (newCardsId != null) return newCardsId;
        throw new UnsupportedOperationException("both are null cannot be called");
    }


}
