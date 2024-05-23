package it.polimi.ingsw.am11.network.RMI.Client;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.ConnectorInterface;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;

public class ClientToServerConnector implements ConnectorInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientToServerConnector.class);

    private final ClientViewUpdater clientUpdater;

    public ClientToServerConnector(ClientViewUpdater viewUpdater) {
        this.clientUpdater = viewUpdater;
    }

    @Override
    public void updateHand(int cardId, boolean removeMode) {
        clientUpdater.updateHand(cardId, removeMode);
    }

    @Override
    public void updatePersonalObjective(int cardId, boolean removeMode) {
        clientUpdater.updatePersonalObjective(cardId, removeMode);

    }

    @Override
    public void receiveStarterCard(int cardId) {
        clientUpdater.receiveStarterCard(cardId);

    }

    @Override
    public void receiveCandidateObjective(Set<Integer> cardsId) {
        clientUpdater.receiveCandidateObjective(cardsId);

    }

    @Override
    public void updateDeckTop(PlayableCardType type, Color color) {
        clientUpdater.updateDeckTop(type, color);

    }

    @Override
    public void updateField(String nickname, int x, int y, int cardId, boolean isRetro,
                            boolean removeMode) {
        clientUpdater.updateField(nickname, x, y, cardId, isRetro, removeMode);

    }

    @Override
    public void updateShownPlayable(Integer previousId, Integer currentId) {
        clientUpdater.updateShownPlayable(previousId, currentId);
    }

    @Override
    public void updateTurnChange(String nickname) {
        clientUpdater.updateTurnChange(nickname);

    }

    @Override
    public void updatePlayerPoint(String nickname, int points) {
        clientUpdater.updatePlayerPoint(nickname, points);

    }

    @Override
    public void updateGameStatus(GameStatus status) {
        clientUpdater.updateGameStatus(status);

    }

    @Override
    public void sendFinalLeaderboard(Map<String, Integer> finalLeaderboard) {
        clientUpdater.receiveFinalLeaderboard(finalLeaderboard);

    }

    @Override
    public void updateCommonObjective(Set<Integer> cardId, boolean removeMode) {
        clientUpdater.updateCommonObjective(cardId, removeMode);

    }

    @Override
    public void updatePlayers(Map<PlayerColor, String> currentPlayers) throws RemoteException {
        clientUpdater.updatePlayers(currentPlayers);
    }

    @Override
    public void notifyGodPlayer() throws RemoteException {
        LOGGER.debug("Notifying god player");
        clientUpdater.notifyGodPlayer();
    }

    @Override
    public void updateNumOfPlayers(int numberOfPlayers) throws RemoteException {
        clientUpdater.updateNumOfPlayers(numberOfPlayers);
    }
}
