package it.polimi.ingsw.am11.view.client.GUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.view.client.ClientChatUpdater;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import it.polimi.ingsw.am11.view.client.ExceptionThrower;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.SequencedMap;
import java.util.Set;

public class GuiUpdater implements ClientViewUpdater, ClientChatUpdater {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuiUpdater.class);

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
    public void updateDeckTop(@NotNull PlayableCardType type, Color color) {
        guiObserver.updateDeckTop(type, color);
        miniGameModel.table().refreshDeckTop(type, color);
    }

    @Override
    public void updateField(@NotNull String nickname, int x, int y, int cardId, boolean isRetro,
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
    public void updateTurnChange(@NotNull String nickname) {
        guiObserver.updateTurnChange(nickname);
        miniGameModel.setCurrentTurn(nickname);
    }

    @Override
    public void updatePlayerPoint(@NotNull String nickname, int points) {
        miniGameModel.addPoints(nickname, points);
        guiObserver.updatePlayerPoint(nickname, points);
    }

    @Override
    public void updateGameStatus(@NotNull GameStatus status) {
        guiObserver.updateGameStatus(status);
        miniGameModel.table().setStatus(status);
    }

    @Override
    public void updateCommonObjective(@NotNull Set<Integer> cardId, boolean removeMode) {
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
    public void receiveFinalLeaderboard(@NotNull Map<String, Integer> finalLeaderboard) {
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
        LOGGER.debug("Received starter card: {}", cardId);
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
    public void updatePlayers(@NotNull SequencedMap<PlayerColor, String> currentPlayers) {
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
        return this;
    }

    @Override
    public void receiveMsg(@NotNull String sender, @NotNull String msg) {

    }

    @Override
    public void receivePrivateMsg(@NotNull String sender, @NotNull String msg) {

    }

    @Override
    public void confirmSentMsg(@NotNull String sender, @NotNull String msg) {
        //TODO confimation of the message sent
    }
}
