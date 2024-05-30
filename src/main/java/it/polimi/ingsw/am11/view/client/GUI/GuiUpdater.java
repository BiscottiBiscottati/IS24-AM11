package it.polimi.ingsw.am11.view.client.GUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.view.client.ClientChatUpdater;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import it.polimi.ingsw.am11.view.client.ExceptionThrower;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;

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
        miniGameModel.table().refreshDeckTop(type, color);
    }

    @Override
    public void updateField(String nickname, int x, int y, int cardId, boolean isRetro,
                            boolean removeMode) {
        guiObserver.updateField(nickname, x, y, cardId, isRetro, removeMode);

    }

    @Override
    public void updateShownPlayable(Integer previousId, Integer currentId) {
        guiObserver.updateShownPlayable(previousId, currentId);
        miniGameModel.table().pickVisible(previousId);
        miniGameModel.table().addVisible(currentId);
    }

    @Override
    public void updateTurnChange(String nickname) {
        guiObserver.updateTurnChange(nickname);
        miniGameModel.setCurrentTurn(nickname);
    }

    @Override
    public void updatePlayerPoint(String nickname, int points) {
        miniGameModel.addPoints(nickname, points);
        guiObserver.updatePlayerPoint(nickname, points);
    }

    @Override
    public void updateGameStatus(GameStatus status) {
        guiObserver.updateGameStatus(status);
        miniGameModel.table().setStatus(status);
    }

    @Override
    public void updateCommonObjective(Set<Integer> cardId, boolean removeMode) {
        if (removeMode) {
            for (int id : cardId) {
                miniGameModel.table().removeCommonObjective(id);
            }
        } else {
            for (int id : cardId) {
                miniGameModel.table().addCommonObjectives(id);
            }
        }
        guiObserver.updateCommonObjective(cardId, removeMode);
    }

    @Override
    public void receiveFinalLeaderboard(Map<String, Integer> finalLeaderboard) {
        miniGameModel.setFinalLeaderboard(finalLeaderboard);
        guiObserver.receiveFinalLeaderboard(finalLeaderboard);
    }

    @Override
    public void updateHand(int cardId, boolean removeMode) {
        if (removeMode) {
            miniGameModel.removeCardFromHand(cardId);
        } else {
            miniGameModel.addCardInHand(cardId);
        }
        guiObserver.updateHand(cardId, removeMode);
    }

    @Override
    public void updatePersonalObjective(int cardId, boolean removeMode) {
        if (removeMode) {
            miniGameModel.rmPersonalObjective(cardId);
        } else {
            miniGameModel.addPersonalObjective(cardId);
        }
        guiObserver.updatePersonalObjective(cardId, removeMode);
    }

    @Override
    public void receiveStarterCard(int cardId) {
        miniGameModel.addStarterCard(cardId);
        guiObserver.receiveStarterCard(cardId);
    }

    @Override
    public void receiveCandidateObjective(Set<Integer> cardId) {
        miniGameModel.addCandidateObjectives(cardId);
        guiObserver.receiveCandidateObjective(cardId);
    }

    @Override
    public void notifyGodPlayer() {
        String username = miniGameModel.myName();
        miniGameModel.setGodPlayer(username);
        guiObserver.notifyGodPlayer();
    }

    @Override
    public void updatePlayers(Map<PlayerColor, String> currentPlayers) {
        guiObserver.updatePlayers(currentPlayers);
        for (Map.Entry<PlayerColor, String> entry : currentPlayers.entrySet()) {
            miniGameModel.addPlayer(entry.getValue(), entry.getKey());
        }
    }

    @Override
    public void updateNumOfPlayers(int numOfPlayers) {
        guiObserver.updateNumOfPlayers(numOfPlayers);
    }

    @Override
    public void disconnectedFromServer(@NotNull String message) {
        guiObserver.disconnectedFromServer();
    }

    @Override
    public @NotNull ExceptionThrower getExceptionThrower() {
        return exceptionReceiver;
    }

    @Override
    public @NotNull ClientChatUpdater getChatUpdater() {
        //  FIXME to implement
        return null;
    }
}
