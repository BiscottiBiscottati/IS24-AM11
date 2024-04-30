package it.polimi.ingsw.am11.view.client;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.table.GameStatus;

import java.util.Set;

// net to client connector

// the network handlers will use (directly) these methods to modify the client view
public class ClientPlayerView {
    private final String playerName;


    public ClientPlayerView(String playerName) {
        this.playerName = playerName;
    }


    public void updateHand(int cardId, boolean removeMode) {

    }


    public void updatePersonalObjective(int cardId, boolean removeMode) {

    }


    public void sendStarterCard(int cardId) {

    }


    public void sendCandidateObjective(Set<Integer> cardsId) {

    }


    public void updateDeckTop(PlayableCardType type, Color color) {

    }


    public void updateField(String nickname, int x, int y, int cardId, boolean removeMode) {

    }


    public void updateShownPlayable(int previousId, int currentId) {

    }


    public void updateTurnChange(String nickname) {

    }


    public void updatePlayerPoint(String nickname, int points) {

    }


    public void updateGameStatus(GameStatus status) {

    }


    public void updateCommonObjective(int cardId, boolean removeMode) {

    }
}
