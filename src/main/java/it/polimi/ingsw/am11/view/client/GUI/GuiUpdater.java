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
import it.polimi.ingsw.am11.view.client.GUI.windows.SetNickPage;
import it.polimi.ingsw.am11.view.client.GUI.windows.SetObjCardsPage;
import it.polimi.ingsw.am11.view.client.GUI.windows.SetStarterCardsPage;
import it.polimi.ingsw.am11.view.client.GUI.windows.WaitingRoomPage;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import it.polimi.ingsw.am11.view.client.miniModel.exceptions.SyncIssueException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.SequencedMap;
import java.util.Set;


/**
 * This class is responsible for updating the GUI based on the events received from the server
 */
public class GuiUpdater implements ClientViewUpdater, ClientChatUpdater {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuiUpdater.class);
    private final CodexNaturalis codexNaturalis;
    MiniGameModel miniGameModel;
    private GuiExceptionReceiver exceptionReceiver;
    private String candidateNick = "";

    /**
     * Create a new GuiUpdater
     * <p>
     *
     * @param codexNaturalis the GUI that needs to be updated
     */
    public GuiUpdater(@NotNull CodexNaturalis codexNaturalis) {
        this.codexNaturalis = codexNaturalis;
        reset();
    }

    /**
     * Reset the GUiUpdater to the initial state
     */
    public void reset() {
        this.miniGameModel = new MiniGameModel();
        this.exceptionReceiver = new GuiExceptionReceiver(miniGameModel, codexNaturalis);
    }

    /**
     * Update the GUI with the new card that has been picked from the deck
     * <p>
     *
     * @param type  the type of the deck
     * @param color the color of the card
     */
    @Override
    public void updateDeckTop(@NotNull PlayableCardType type, @NotNull Color color) {
        LOGGER.debug("{} picked a card from the {} deck, the {} deck top card is now {}",
                     miniGameModel.getCurrentTurn(), type.getName(), type.getName(),
                     color.getColumnName());

        miniGameModel.table().refreshDeckTop(type, color);
        codexNaturalis.updateDeckTop(type, color);
    }

    /**
     * Update the GUI with the new card that has been picked from the hand placed on the table
     * <p>
     *
     * @param nickname the nickname of the player
     * @param x        the x coordinate of the card
     * @param y        the y coordinate of the card
     * @param cardId   the id of the card
     * @param isRetro  if the card is placed on it's retro
     */
    @Override
    public void updateField(@NotNull String nickname, int x, int y, int cardId, boolean isRetro) {
        LOGGER.debug("updateField: Nickname: {}, X: {}, Y: {}, cardId: {}, isRetro: {},",
                     nickname, x, y, cardId, isRetro);
        try {
            miniGameModel.getCliPlayer(nickname).getField()
                         .place(new Position(x, y), cardId, isRetro);
        } catch (SyncIssueException e) {
            throw new RuntimeException(e);
        }
        codexNaturalis.updateField(nickname, x, y, cardId, isRetro);

    }

    @Override
    public void updateShownPlayable(Integer previousId, Integer currentId) {
        LOGGER.debug("Removed from visible: {}, Added: {}", previousId, currentId);
        if (previousId != null) miniGameModel.table().pickVisible(previousId);
        if (currentId != null) miniGameModel.table().addVisible(currentId);
        codexNaturalis.updateShownPlayable(previousId, currentId);

    }

    @Override
    public void updateTurnChange(@NotNull String nickname) {
        miniGameModel.setCurrentTurn(nickname);
        LOGGER.debug("It's {} turn", nickname);

        codexNaturalis.updateTurnChange(nickname);
    }

    @Override
    public void updatePlayerPoint(@NotNull String nickname, int points) {
        miniGameModel.getCliPlayer(nickname).addPoints(points);
        LOGGER.debug("{} points added to {}", points, nickname);

        codexNaturalis.updatePlayerPoint(nickname, points);
    }

    @Override
    public void updateGameStatus(@NotNull GameStatus status) {
        miniGameModel.table().setStatus(status);
        LOGGER.debug("Game status event: {}", status);
        codexNaturalis.updateGameStatus(status);
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

        codexNaturalis.updateCommonObjective(cardId, removeMode);
    }

    @Override
    public void receiveFinalLeaderboard(@NotNull Map<String, Integer> finalLeaderboard) {
        LOGGER.debug("Final leaderboard received");
        miniGameModel.setFinalLeaderboard(finalLeaderboard);
        codexNaturalis.receiveFinalLeaderboard(finalLeaderboard);
    }

    @Override
    public void updateHand(int cardId, boolean removeMode) {
        LOGGER.debug("UpdateHand, cardId: {}, removeMode: {}", cardId, removeMode);
        if (removeMode) {
            miniGameModel.removeCardFromHand(cardId);
        } else {
            miniGameModel.addCardInHand(cardId);
            // FIXME check if it is not necessary to use iPlaced
            // miniGameModel.setIPlaced(false);
        }

        codexNaturalis.updateHand(cardId, removeMode);
    }

    @Override
    public void updatePersonalObjective(int cardId, boolean removeMode) {
        LOGGER.debug("UpPersonalObj, cardId: {}, removeMode: {}", cardId, removeMode);

        if (removeMode) {
            miniGameModel.rmPersonalObjective(cardId);
        } else {
            miniGameModel.addPersonalObjective(cardId);
        }
        codexNaturalis.updatePersonalObjective(cardId, removeMode);
    }

    @Override
    public void receiveStarterCard(int cardId) {
        LOGGER.debug("Received starter card: {}", cardId);
        miniGameModel.addStarterCard(cardId);
        codexNaturalis.receiveStarterCard();
    }

    @Override
    public void receiveCandidateObjective(@NotNull Set<Integer> cardId) {
        cardId.forEach(
                x -> LOGGER.debug("Receive candidate objective event, card id: {}", x));

        miniGameModel.addCandidateObjectives(cardId);

        CodexNaturalis.receiveCandidateObjective();
    }

    @Override
    public void notifyGodPlayer() {
        LOGGER.debug("EVENT: God player notification");

        miniGameModel.setMyName(candidateNick);
        miniGameModel.setGodPlayer(candidateNick);

        CodexNaturalis.notifyGodPlayer();
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
    }

    @Override
    public void updateNumOfPlayers(int numOfPlayers) {
    }

    @Override
    public void disconnectedFromServer(@NotNull String message) {
        LOGGER.error("Disconnected from server: {}", message);
        codexNaturalis.disconnectedFromServer();
    }

    @Override
    public void receiveReconnection(@NotNull ReconnectionModelMemento memento) {
        LOGGER.debug("Reconnection event received");
        miniGameModel.load(memento);
        miniGameModel.setMyName(candidateNick);

        //Set Gui
        switch (miniGameModel.table().getStatus()) {
            case CHOOSING_STARTERS -> {
                WaitingRoomPage.hideWaitingRoomPage();
                codexNaturalis.receiveStarterCard();
                SetStarterCardsPage.showStarterCardsPage();
            }
            case CHOOSING_OBJECTIVES -> {
                WaitingRoomPage.hideWaitingRoomPage();
                CodexNaturalis.receiveCandidateObjective();
                SetObjCardsPage.showObjCardsPage();
            }
            case SETUP -> {
                WaitingRoomPage.hideWaitingRoomPage();
                SetNickPage.showSettingNickPage();
            }
            case ONGOING, ENDED, ARMAGEDDON, LAST_TURN ->
                    codexNaturalis.reconnectedToServer(miniGameModel.table().getStatus());
        }

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
        codexNaturalis.updateChat();
    }

    @Override
    public void receivePrivateMsg(@NotNull String sender, @NotNull String msg) {
        miniGameModel.addChatMessage("[PRIVATE] " + sender + ": " + msg);
        codexNaturalis.updateChat();
    }

    @Override
    public void confirmSentMsg(@NotNull String sender, @NotNull String msg) {
        LOGGER.debug("Message sent confirmation received");
        if (sender.equals(miniGameModel.myName())) {
            miniGameModel.addChatMessage("[YOU] " + sender + ": " + msg);
        }
        codexNaturalis.updateChat();
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
