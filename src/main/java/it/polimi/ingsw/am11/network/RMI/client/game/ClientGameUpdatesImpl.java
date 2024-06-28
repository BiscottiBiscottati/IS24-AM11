package it.polimi.ingsw.am11.network.RMI.client.game;

import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.model.utils.memento.ReconnectionModelMemento;
import it.polimi.ingsw.am11.network.RMI.client.ClientRMI;
import it.polimi.ingsw.am11.network.RMI.remote.game.ClientGameUpdatesInterface;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.SequencedMap;
import java.util.Set;

/**
 * This class is the implementation of the ClientGameUpdatesInterface.
 * It is used by the server to send updates to the client.
 * <p>
 *     The view updater is the object that will update the view of the client.
 *     Implements the {@link ClientGameUpdatesInterface} interface.
 * </p>
 * @param viewUpdater The view updater
 * @param clientRMI The client RMI
 */
public record ClientGameUpdatesImpl(@NotNull ClientViewUpdater viewUpdater,
                                    @NotNull ClientRMI clientRMI)
        implements ClientGameUpdatesInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientGameUpdatesImpl.class);

    /**
     * Updates the hand of the player
     * @param cardId The card id
     * @param removeMode The remove mode
     */
    @Override
    public void updateHand(int cardId, boolean removeMode) {
        viewUpdater.updateHand(cardId, removeMode);
    }

    /**
     * Updates the personal objective of the player
     * @param cardId The card id
     * @param removeMode The remove mode
     */
    @Override
    public void updatePersonalObjective(int cardId, boolean removeMode) {
        viewUpdater.updatePersonalObjective(cardId, removeMode);

    }

    /**
     * Receives the starter card
     * @param cardId The card id
     */
    @Override
    public void receiveStarterCard(int cardId) {
        viewUpdater.receiveStarterCard(cardId);

    }

    /** Receives the candidate objective
     * @param cardsId The cards id
     */
    @Override
    public void receiveCandidateObjective(@NotNull Set<Integer> cardsId) {
        viewUpdater.receiveCandidateObjective(cardsId);

    }

    @Override
    public void updateDeckTop(@NotNull PlayableCardType type, @NotNull GameColor color) {
        viewUpdater.updateDeckTop(type, color);

    }

    @Override
    public void updateField(@NotNull String nickname, int x, int y, int cardId, boolean isRetro) {
        viewUpdater.updateField(nickname, x, y, cardId, isRetro);
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
    public void updatePlayers(@NotNull SequencedMap<PlayerColor, String> currentPlayers)
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

    @Override
    public void sendReconnection(@NotNull ReconnectionModelMemento memento) throws RemoteException {
        viewUpdater.receiveReconnection(memento);
    }

    @Override
    public void youUgly() {
        viewUpdater.disconnectedFromServer("Game ended!");
        clientRMI.close();
    }


}
