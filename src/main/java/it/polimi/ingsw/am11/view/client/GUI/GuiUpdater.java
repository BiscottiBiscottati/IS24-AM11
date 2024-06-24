package it.polimi.ingsw.am11.view.client.GUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.model.utils.memento.ReconnectionModelMemento;
import it.polimi.ingsw.am11.view.client.ClientChatUpdater;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import it.polimi.ingsw.am11.view.client.ExceptionThrower;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import it.polimi.ingsw.am11.view.client.miniModel.exceptions.SyncIssueException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.SequencedMap;
import java.util.Set;

public class GuiUpdater implements ClientViewUpdater, ClientChatUpdater {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuiUpdater.class);
    private final GuiObserver guiObserver;
    MiniGameModel miniGameModel;
    private GuiExceptionReceiver exceptionReceiver;
    private String candidateNick = "";

    public GuiUpdater(@NotNull GuiObserver guiObserver) {
        this.guiObserver = guiObserver;
        reset();
    }

    /**
     * Reset the GUiUpdater to the initial state
     */
    public void reset() {
        this.miniGameModel = new MiniGameModel();
        this.exceptionReceiver = new GuiExceptionReceiver(miniGameModel, guiObserver);
    }

    @Override
    public void updateDeckTop(@NotNull PlayableCardType type, @NotNull Color color) {
        LOGGER.debug("{} picked a card from the {} deck, the {} deck top card is now {}",
                     miniGameModel.getCurrentTurn(), type.getName(), type.getName(),
                     color.getColumnName());

        miniGameModel.table().refreshDeckTop(type, color);
        guiObserver.updateDeckTop(type, color);
    }

    @Override
    public void updateField(@NotNull String nickname, int x, int y, int cardId, boolean isRetro) {
        LOGGER.debug("updateField: Nickname: {}, X: {}, Y: {}, cardId: {}, isRetro: {},",
                     nickname, x, y, cardId, isRetro);
        try {
            miniGameModel.getCliPlayer(nickname).getField()
                         .place(new Position(x, y), cardId, isRetro);
        } catch (SyncIssueException e) {
            //TODO handle this exception
            throw new RuntimeException(e);
        }

        guiObserver.updateField(nickname, x, y, cardId, isRetro);

    }

    @Override
    public void updateShownPlayable(Integer previousId, Integer currentId) {
        LOGGER.debug("Removed from visible: {}, Added: {}", previousId, currentId);
        if (previousId != null) miniGameModel.table().pickVisible(previousId);
        if (currentId != null) miniGameModel.table().addVisible(currentId);
        guiObserver.updateShownPlayable(previousId, currentId);

    }

    @Override
    public void updateTurnChange(@NotNull String nickname) {
        miniGameModel.setCurrentTurn(nickname);
        LOGGER.debug("It's {} turn", nickname);

        guiObserver.updateTurnChange(nickname);
    }

    @Override
    public void updatePlayerPoint(@NotNull String nickname, int points) {
        miniGameModel.getCliPlayer(nickname).addPoints(points);
        LOGGER.debug("{} points added to {}", points, nickname);

        guiObserver.updatePlayerPoint(nickname, points);
    }

    @Override
    public void updateGameStatus(@NotNull GameStatus status) {
        miniGameModel.table().setStatus(status);
        LOGGER.debug("Game status event: {}", status);
        try {
            guiObserver.updateGameStatus(status);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCommonObjective(@NotNull Set<Integer> cardId, boolean removeMode) {
        if (removeMode) {
            cardId.forEach(x -> miniGameModel.table().removeCommonObjective(x));
            LOGGER.debug("CommonObjRm: {}", cardId);
        } else {
            cardId.forEach(x -> miniGameModel.table().addCommonObjectives(x));
            LOGGER.debug("CommonObjAdd: {}", cardId);
        }

        guiObserver.updateCommonObjective(cardId, removeMode);
    }

    @Override
    public void receiveFinalLeaderboard(@NotNull Map<String, Integer> finalLeaderboard) {
        LOGGER.debug("Final leaderboard received");
        miniGameModel.setFinalLeaderboard(finalLeaderboard);

        guiObserver.receiveFinalLeaderboard(finalLeaderboard);
    }

    @Override
    public void updateHand(int cardId, boolean removeMode) {
        LOGGER.debug("UpdateHand, cardId: {}, removeMode: {}", cardId, removeMode);
        if (removeMode) {
            miniGameModel.removeCardFromHand(cardId);
        } else {
            miniGameModel.addCardInHand(cardId);
            // FIXME check if it is not necessary to use iPlaced
            // miniGameModel.setiPlaced(false);
        }

        guiObserver.updateHand(cardId, removeMode);
    }

    @Override
    public void updatePersonalObjective(int cardId, boolean removeMode) {
        LOGGER.debug("UpPersonalObj, cardId: {}, removeMode: {}", cardId, removeMode);

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
    public void receiveCandidateObjective(@NotNull Set<Integer> cardId) {
        cardId.forEach(
                x -> LOGGER.debug("Receive candidate objective event, card id: {}", x));

        miniGameModel.addCandidateObjectives(cardId);

        guiObserver.receiveCandidateObjective(cardId);
    }

    @Override
    public void notifyGodPlayer() {
        LOGGER.debug("EVENT: God player notification");

        miniGameModel.setMyName(candidateNick);
        miniGameModel.setGodPlayer(candidateNick);

        guiObserver.notifyGodPlayer();
    }

    @Override
    public void updatePlayers(@NotNull SequencedMap<PlayerColor, String> currentPlayers) {
        if (! currentPlayers.containsValue(candidateNick)) {
            LOGGER.error("Candidate nick not found in the player list");
            throw new RuntimeException("Candidate nick not found in the player list");
        }
        miniGameModel.setMyName(candidateNick);
        currentPlayers.sequencedKeySet()
                      .forEach(x -> miniGameModel.addPlayer(currentPlayers.get(x), x));

        guiObserver.updatePlayers(currentPlayers);
    }

    @Override
    public void updateNumOfPlayers(int numOfPlayers) {
        guiObserver.updateNumOfPlayers(numOfPlayers);
    }

    @Override
    public void disconnectedFromServer(@NotNull String message) {
        LOGGER.error("Disconnected from server: {}", message);
        guiObserver.disconnectedFromServer();
    }

    @Override
    public void receiveReconnection(@NotNull ReconnectionModelMemento memento) {
        LOGGER.debug("Reconnection event received");
        miniGameModel.load(memento);

        //FIXME I don't know if candidate nick is used
        miniGameModel.setMyName(candidateNick);

        //TODO update the gui to the correct state

        LOGGER.debug("Reconnection completed");
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
        miniGameModel.addChatMessage("[PUBLIC] " + sender + ": " + msg);
        //TODO implement in gui
    }

    @Override
    public void receivePrivateMsg(@NotNull String sender, @NotNull String msg) {
        miniGameModel.addChatMessage("[PRIVATE] " + sender + ": " + msg);
        //TODO implement in gui
    }

    @Override
    public void confirmSentMsg(@NotNull String sender, @NotNull String msg) {
        if (sender.equals(miniGameModel.myName())) {
            miniGameModel.addChatMessage("[YOU] " + sender + ": " + msg);
        }
        //TODO implement in gui
    }

    public MiniGameModel getMiniGameModel() {
        return miniGameModel;
    }

    public String getCandidateNick() {
        return candidateNick;
    }

    public void setCandidateNick(String candidateNick) {
        this.candidateNick = candidateNick;
    }
}
