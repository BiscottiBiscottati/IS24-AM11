package it.polimi.ingsw.am11.network.RMI.Client;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.ClientGameConnector;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;

public class NetworkConnector implements ClientGameConnector {
    private final ClientRMI main;
    private String nickname;

    public NetworkConnector(@NotNull ClientRMI main) {
        this.main = main;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void setNickname(String nickname) {
        try {
            main.login(nickname);
            this.nickname = nickname;
        } catch (RemoteException e) {
            throw new RuntimeException(e);
            // disconnection problem?
        }

    }

    @Override
    public void setStarterCard(boolean isRetro) {
        try {
            main.setStarterCard(nickname, isRetro);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void setPersonalObjective(int cardId) {
        try {
            main.setObjectiveCard(nickname, cardId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void placeCard(@NotNull Position pos, int cardId, boolean isRetro) {
        try {
            main.placeCard(nickname, cardId, pos.x(), pos.y(), isRetro);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void drawCard(boolean fromVisible, PlayableCardType type, int cardId) {
        try {
            main.drawCard(nickname, fromVisible, type, cardId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void setNumOfPlayers(int numOfPlayers) {
        try {
            main.setNumOfPlayers(nickname, numOfPlayers);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }
}
