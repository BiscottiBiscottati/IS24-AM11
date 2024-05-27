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
    private final GuiObserver guiObserver;
    MiniGameModel miniGameModel;

    public GuiUpdater(GuiExceptionReceiver exceptionReceiver, MiniGameModel miniGameModel,
                      GuiObserver guiObserver) {
        this.exceptionReceiver = exceptionReceiver;
        this.miniGameModel = miniGameModel;
        this.guiObserver = guiObserver;
    }

    @Override
    public void updateDeckTop(PlayableCardType type, Color color) {
        guiObserver.updateDeckTop(type, color);
    }

    @Override
    public void updateField(String nickname, int x, int y, int cardId, boolean isRetro,
                            boolean removeMode) {
        guiObserver.updateField(nickname, x, y, cardId, isRetro, removeMode);

    }

    @Override
    public void updateShownPlayable(Integer previousId, Integer currentId) {
        guiObserver.updateShownPlayable(previousId, currentId);
    }

    @Override
    public void updateTurnChange(String nickname) {
        guiObserver.updateTurnChange(nickname);
    }

    @Override
    public void updatePlayerPoint(String nickname, int points) {
        guiObserver.updatePlayerPoint(nickname, points);
    }

    @Override
    public void updateGameStatus(GameStatus status) {
        guiObserver.updateGameStatus(status);
    }

    @Override
    public void updateCommonObjective(Set<Integer> cardId, boolean removeMode) {
        guiObserver.updateCommonObjective(cardId, removeMode);
    }

    @Override
    public void receiveFinalLeaderboard(Map<String, Integer> finalLeaderboard) {
        guiObserver.receiveFinalLeaderboard(finalLeaderboard);
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
