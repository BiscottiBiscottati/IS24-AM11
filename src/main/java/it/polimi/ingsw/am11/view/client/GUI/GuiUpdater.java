package it.polimi.ingsw.am11.view.client.GUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import it.polimi.ingsw.am11.view.client.ExceptionConnector;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;

import java.util.Map;
import java.util.Set;

public class GuiUpdater implements ClientViewUpdater {
    private final GuiExceptionReceiver exceptionReceiver;
    MiniGameModel miniGameModel;

    public GuiUpdater(GuiExceptionReceiver exceptionReceiver, MiniGameModel miniGameModel) {
        this.exceptionReceiver = exceptionReceiver;
        this.miniGameModel = miniGameModel;
    }

    @Override
    public void updateDeckTop(PlayableCardType type, Color color) {

    }

    @Override
    public void updateField(String nickname, int x, int y, int cardId, boolean isRetro,
                            boolean removeMode) {

    }

    @Override
    public void updateShownPlayable(Integer previousId, Integer currentId) {

    }

    @Override
    public void updateTurnChange(String nickname) {

    }

    @Override
    public void updatePlayerPoint(String nickname, int points) {

    }

    @Override
    public void updateGameStatus(GameStatus status) {

    }

    @Override
    public void updateCommonObjective(Set<Integer> cardId, boolean removeMode) {

    }

    @Override
    public void receiveFinalLeaderboard(Map<String, Integer> finalLeaderboard) {

    }

    @Override
    public void updateHand(int cardId, boolean removeMode) {

    }

    @Override
    public void updatePersonalObjective(int cardId, boolean removeMode) {

    }

    @Override
    public void receiveStarterCard(int cardId) {

    }

    @Override
    public void receiveCandidateObjective(Set<Integer> cardId) {

    }

    @Override
    public void notifyGodPlayer() {
        String username = miniGameModel.myName();
        miniGameModel.setGodPlayer(username);
    }

    @Override
    public void updatePlayers(Map<PlayerColor, String> currentPlayers) {

    }

    @Override
    public void updateNumOfPlayers(int numOfPlayers) {

    }

    @Override
    public void disconnectedFromServer() {

    }

    @Override
    public ExceptionConnector getExceptionConnector() {
        return exceptionReceiver;
    }
}
