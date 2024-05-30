package it.polimi.ingsw.am11.network.RMI.client.game;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.network.RMI.remote.game.ClientGameUpdatesInterface;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;

public record ClientGameUpdatesImpl(@NotNull ClientViewUpdater viewUpdater)
        implements ClientGameUpdatesInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientGameUpdatesImpl.class);

    @Override
    public void updateHand(int cardId, boolean removeMode) {
        viewUpdater.updateHand(cardId, removeMode);
    }

    @Override
    public void updatePersonalObjective(int cardId, boolean removeMode) {
        viewUpdater.updatePersonalObjective(cardId, removeMode);

    }

    @Override
    public void receiveStarterCard(int cardId) {
        viewUpdater.receiveStarterCard(cardId);

    }

    @Override
    public void receiveCandidateObjective(@NotNull Set<Integer> cardsId) {
        viewUpdater.receiveCandidateObjective(cardsId);

    }

    @Override
    public void updateDeckTop(@NotNull PlayableCardType type, @NotNull Color color) {
        viewUpdater.updateDeckTop(type, color);

    }

    @Override
    public void updateField(@NotNull String nickname, int x, int y, int cardId, boolean isRetro,
                            boolean removeMode) {
        viewUpdater.updateField(nickname, x, y, cardId, isRetro, removeMode);

    }

    @Override
    public void updateShownPlayable(Integer previousId, Integer currentId) {
        viewUpdater.updateShownPlayable(previousId, currentId);
    }

    @Override
    public void updateTurnChange(@NotNull String nickname) {
        viewUpdater.updateTurnChange(nickname);

    }

    @Override
    public void updatePlayerPoint(@NotNull String nickname, int points) {
        viewUpdater.updatePlayerPoint(nickname, points);

    }

    @Override
    public void updateGameStatus(@NotNull GameStatus status) {
        viewUpdater.updateGameStatus(status);

    }

    @Override
    public void sendFinalLeaderboard(@NotNull Map<String, Integer> finalLeaderboard) {
        viewUpdater.receiveFinalLeaderboard(finalLeaderboard);

    }

    @Override
    public void updateCommonObjective(@NotNull Set<Integer> cardId, boolean removeMode) {
        viewUpdater.updateCommonObjective(cardId, removeMode);

    }

    @Override
    public void updatePlayers(@NotNull Map<PlayerColor, String> currentPlayers)
    throws RemoteException {
        viewUpdater.updatePlayers(currentPlayers);
    }

    @Override
    public void notifyGodPlayer() throws RemoteException {
        LOGGER.debug("CLIENT RMI: Notifying god player");
        viewUpdater.notifyGodPlayer();
    }

    @Override
    public void updateNumOfPlayers(int numberOfPlayers) throws RemoteException {
        viewUpdater.updateNumOfPlayers(numberOfPlayers);
    }
}
