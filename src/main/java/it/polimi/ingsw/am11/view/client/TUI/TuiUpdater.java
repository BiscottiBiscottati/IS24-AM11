package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.model.utils.memento.ReconnectionModelMemento;
import it.polimi.ingsw.am11.view.client.ClientChatUpdater;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import it.polimi.ingsw.am11.view.client.ExceptionThrower;
import it.polimi.ingsw.am11.view.client.TUI.states.TUIState;
import it.polimi.ingsw.am11.view.client.TUI.states.TuiStates;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import it.polimi.ingsw.am11.view.client.miniModel.exceptions.SyncIssueException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;
import java.util.SequencedMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class is the implementation of the ClientViewUpdater and the ExceptionConnector. The classes
 * that handle the interpretation of the messages from the net will call these methods. There are
 * also methods designed to be used by the Actuator (and possibly other classes) to update and get
 * the TUIState and to save the candidateNick (the nickname that the player tries to send to the
 * server)
 */

public class
TuiUpdater implements ClientViewUpdater, ClientChatUpdater {
    private static final Logger LOGGER = LoggerFactory.getLogger(TuiUpdater.class);
    private final @NotNull EnumMap<TuiStates, TUIState> tuiStates;
    private MiniGameModel model;
    private TuiExceptionReceiver exceptionReceiver;
    private AtomicReference<TUIState> currentState;
    private AtomicReference<TUIState> homeState;
    private String candidateNick = "";

    public TuiUpdater(TuiStates startingState) {
        this.tuiStates = new EnumMap<>(TuiStates.class);
        reset(startingState);
        currentState.get().restart(false, null);
    }

    /**
     * Reset the TuiUpdater to the initial state
     *
     * @param startingState the state to start from
     */
    public void reset(TuiStates startingState) {
        this.model = new MiniGameModel();
        for (TuiStates state : TuiStates.values()) {
            tuiStates.put(state, state.getNewState(model));
        }
        this.currentState = new AtomicReference<>(tuiStates.get(startingState));
        this.exceptionReceiver = new TuiExceptionReceiver(model, this);
        this.homeState = new AtomicReference<>();
        this.homeState.set(tuiStates.get(startingState));
    }

    /**
     * Update the deck top card
     *
     * @param type  the type of the deck
     * @param color the color of the card
     */
    @Override
    public void updateDeckTop(@NotNull PlayableCardType type, @Nullable GameColor color) {
        LOGGER.debug("{} picked a card from the {} deck, the {} deck top card is now {}",
                     model.getCurrentTurn(), type.getName(), type.getName(), color);

        model.table().refreshDeckTop(type, color);

        if (isCurrentState(TuiStates.WATCHING_TABLE)) {
            currentState.get().restart(false, null);
        }
    }

    /**
     * Update the field of the player, it can place or remove a card, if the removeMode is true the
     * cardId and isRetro are ignored.
     *
     * @param nickname the nickname of the player
     * @param x        the x coordinate of the card
     * @param y        the y coordinate of the card
     * @param cardId   the id of the card
     * @param isRetro  if the card is placed on it's retro
     */
    @Override
    public void updateField(@NotNull String nickname, int x, int y, int cardId,
                            boolean isRetro) throws SyncIssueException {
        LOGGER.debug("updateField: Nickname: {}, X: {}, Y: {}, cardId: {}, isRetro: {}",
                     nickname, x, y, cardId, isRetro);
        Position pos = new Position(x, y);
        model.getCliPlayer(nickname).getField().place(pos, cardId, isRetro);

        if (isCurrentState(TuiStates.WATCHING_FIELD)) {
            String[] args = {"notify", nickname};
            currentState.get().passArgs(null, args);
        }
    }

    /**
     * Update the visible cards on the table, it will remove the previousId and add the currentId
     *
     * @param previousId the id of the card to remove
     * @param currentId  the id of the card to add
     */
    @Override
    public void updateShownPlayable(@Nullable Integer previousId, @Nullable Integer currentId) {
        if (previousId != null) model.table().pickVisible(previousId);
        if (currentId != null) model.table().addVisible(currentId);
        LOGGER.debug("Removed from visible: {}, Added: {}", previousId, currentId);

        if (isCurrentState(TuiStates.WATCHING_TABLE)) {
            currentState.get().restart(false, null);
        }
    }

    /**
     * Update the current turn of the game
     *
     * @param nickname the nickname of the player that has the turn
     */
    @Override
    public void updateTurnChange(@NotNull String nickname) {
        model.setCurrentTurn(nickname);
        LOGGER.debug("It's {} turn", nickname);

        if (isCurrentState(TuiStates.WATCHING_TABLE)) {
            currentState.get().restart(false, null);
        }
        assert model.getCurrentTurn() != null;
        if (model.getCurrentTurn().equals(model.myName())) {
            setTuiState(TuiStates.WATCHING_FIELD);
            homeState.set(tuiStates.get(TuiStates.WATCHING_FIELD));
            currentState.get().restart(false, null);
        }
    }

    /**
     * Update the points of a player
     *
     * @param nickname the nickname of the player
     * @param points   the points to add to the already present points
     */
    @Override
    public void updatePlayerPoint(@NotNull String nickname, int points) {
        model.getCliPlayer(nickname).addPoints(points);
        LOGGER.debug("{} points added to {}", points, nickname);
        if (isCurrentState(TuiStates.WATCHING_TABLE)) {
            currentState.get().restart(false, null);
        }
    }

    /**
     * Update the status of the game, it is referred to the status of the model, not the state of
     * the TUI
     *
     * @param status the new status of the game
     */
    @Override
    public void updateGameStatus(@NotNull GameStatus status) {
        model.table().setStatus(status);
        LOGGER.debug("Game status event: {}", status);
        switch (status) {
            case SETUP, ARMAGEDDON, LAST_TURN -> {
            }
            case CHOOSING_STARTERS -> {
                currentState.set(tuiStates.get(TuiStates.CHOOSING_STARTER));
                homeState.set(tuiStates.get(TuiStates.CHOOSING_STARTER));
                currentState.get().restart(false, null);
            }
            case CHOOSING_OBJECTIVES -> {
                currentState.set(tuiStates.get(TuiStates.CHOOSING_OBJECTIVE));
                homeState.set(tuiStates.get(TuiStates.CHOOSING_OBJECTIVE));
                currentState.get().restart(false, null);
            }
            case ENDED -> {
                currentState.set(tuiStates.get(TuiStates.ENDED));
                homeState.set(tuiStates.get(TuiStates.ENDED));
                currentState.get().restart(false, null);
            }
            case ONGOING -> {
                assert model.getCurrentTurn() != null;
                if (model.getCurrentTurn().equals(model.myName())) {
                    setTuiState(TuiStates.WATCHING_FIELD);
                    homeState.set(tuiStates.get(TuiStates.WATCHING_FIELD));
                    currentState.get().restart(false, null);
                } else {
                    currentState.set(tuiStates.get(TuiStates.WATCHING_TABLE));
                    homeState.set(tuiStates.get(TuiStates.WATCHING_TABLE));
                    currentState.get().restart(false, null);
                }
            }
            case null, default ->
                    throw new RuntimeException("Received null or invalid game status update");
        }
    }

    /**
     * Update the common objectives of the table
     *
     * @param cardId     the id of the card to add or remove
     * @param removeMode if you want to remove the card
     */
    @Override
    public void updateCommonObjective(@NotNull Set<Integer> cardId, boolean removeMode) {
        if (removeMode) {
            cardId.forEach(x -> model.table().removeCommonObjective(x));
            LOGGER.debug("CommonObjRm: {}", cardId);
        } else {
            cardId.forEach(x -> model.table().addCommonObjectives(x));
            LOGGER.debug("CommonObjAdd: {}", cardId);
        }

        if (isCurrentState(TuiStates.WATCHING_TABLE)) {
            currentState.get().restart(false, null);
        }
    }

    /**
     * Set the final leaderboard of the game
     *
     * @param finalLeaderboard the final leaderboard
     */
    @Override
    public void receiveFinalLeaderboard(@NotNull Map<String, Integer> finalLeaderboard) {
        model.setFinalLeaderboard(finalLeaderboard);
        LOGGER.debug("Final leaderboard received");
        setTuiState(TuiStates.ENDED);
        homeState.set(tuiStates.get(TuiStates.ENDED));
        currentState.get().restart(false, null);
    }

    /**
     * Update the hand of the player
     *
     * @param cardId     the id of the card to add or remove
     * @param removeMode if you want to remove the card
     */
    @Override
    public void updateHand(int cardId, boolean removeMode) {
        LOGGER.debug("UpdateHand, cardId: {}, removeMode: {}", cardId, removeMode);
        if (removeMode) {
            model.removeCardFromHand(cardId);
        } else {
            model.addCardInHand(cardId);
            model.setIPlaced(false);
        }

        if (isCurrentState(TuiStates.WATCHING_TABLE)) {
            currentState.get().restart(false, null);
        } else if (isCurrentState(TuiStates.WATCHING_FIELD)) {
            String[] args = {"notify", model.myName()};
            currentState.get().passArgs(null, args);
        }
    }

    /**
     * Update the personal objective of the player
     *
     * @param cardId     the id of the card to add or remove
     * @param removeMode if you want to remove the card
     */
    @Override
    public void updatePersonalObjective(int cardId, boolean removeMode) {
        LOGGER.debug("UpPersObj, cardId: {}, removeMode: {}", cardId, removeMode);
        if (removeMode) {
            model.rmPersonalObjective(cardId);
        } else {
            model.addPersonalObjective(cardId);
        }
        if (isCurrentState(TuiStates.WATCHING_TABLE)) {
            currentState.get().restart(false, null);
        }
    }

    /**
     * Receive the starter card of the player
     *
     * @param cardId the id of the card
     */
    @Override
    public void receiveStarterCard(int cardId) {
        model.addStarterCard(cardId);
        LOGGER.debug("Receive starter event, card id: {}", cardId);
    }

    /**
     * Receive the candidate objectives of the player
     *
     * @param cardId Set of card id
     */
    @Override
    public void receiveCandidateObjective(@NotNull Set<Integer> cardId) {
        model.addCandidateObjectives(cardId);
        cardId.forEach(
                x -> LOGGER.debug("Receive candidate objective event, card id: {}", x));
    }

    /**
     * Notify the player that he is the god player, the god player is the moderator of the game. It
     * will set the god player and confirm the player name in the model
     */
    @Override
    public void notifyGodPlayer() {
        LOGGER.debug("EVENT: God player notification");
        model.setMyName(candidateNick);
        model.setGodPlayer(candidateNick);
        currentState.set(tuiStates.get(TuiStates.SETTING_NUM));
        homeState.set(tuiStates.get(TuiStates.SETTING_NUM));
        currentState.get().restart(false, null);
    }

    /**
     * Update the model with the playing players and their colors. It also confirms the player name
     *
     * @param currentPlayers the map of the players and their colors
     */
    @Override
    public void updatePlayers(@NotNull SequencedMap<PlayerColor, String> currentPlayers) {
        if (! currentPlayers.containsValue(candidateNick)) {
            LOGGER.error("Candidate nick not found in the player list");
            throw new RuntimeException("Candidate nick not found in the player list");
        }
        model.setMyName(candidateNick);
        currentPlayers.sequencedKeySet()
                      .forEach(x -> model.addPlayer(currentPlayers.get(x), x));
    }

    /**
     * Update the number of players in the game
     *
     * @param numOfPlayers the number of players
     */
    @Override
    public void updateNumOfPlayers(int numOfPlayers) {

    }

    /**
     * Notify the player that there has been a disconnection from the server
     */
    @Override
    public void disconnectedFromServer(@NotNull String message) {
        LOGGER.error("Disconnected from server: {}", message);
        if (model.table().getStatus() == GameStatus.ENDED) {
            System.exit(0);
        }
        reset(TuiStates.CONNECTING);
        Exception e = new Exception("Disconnected from server: " + message);
        currentState.get().restart(false, e);
    }

    /**
     * Notify the player that there has been a reconnection from the server and update the model to
     * the current situation of the game
     *
     * @param memento data structure that contains the current state of the game
     */
    @Override
    public void receiveReconnection(@NotNull ReconnectionModelMemento memento) {
        LOGGER.debug("Reconnection event received");
        model.load(memento);
        model.setMyName(candidateNick);

        //Set Tui
        switch (model.table().getStatus()) {
            case CHOOSING_STARTERS -> {
                currentState.set(tuiStates.get(TuiStates.CHOOSING_STARTER));
                homeState.set(tuiStates.get(TuiStates.CHOOSING_STARTER));
                currentState.get().restart(false, null);
            }
            case CHOOSING_OBJECTIVES -> {
                currentState.set(tuiStates.get(TuiStates.CHOOSING_OBJECTIVE));
                homeState.set(tuiStates.get(TuiStates.CHOOSING_OBJECTIVE));
                currentState.get().restart(false, null);
            }
            case ENDED -> {
                currentState.set(tuiStates.get(TuiStates.ENDED));
                homeState.set(tuiStates.get(TuiStates.ENDED));
                currentState.get().restart(false, null);
            }
            case ONGOING, ARMAGEDDON, LAST_TURN -> {
                assert model.getCurrentTurn() != null;
                if (model.getCurrentTurn().equals(model.myName()) && ! model.getiPlaced()) {
                    currentState.set(tuiStates.get(TuiStates.WATCHING_FIELD));
                    homeState.set(tuiStates.get(TuiStates.WATCHING_FIELD));
                    currentState.get().restart(false, null);
                } else {
                    currentState.set(tuiStates.get(TuiStates.WATCHING_TABLE));
                    homeState.set(tuiStates.get(TuiStates.WATCHING_TABLE));
                    currentState.get().restart(false, null);
                }
            }
            case SETUP -> {
                currentState.set(tuiStates.get(TuiStates.WAITING));
                homeState.set(tuiStates.get(TuiStates.WAITING));
                currentState.get().restart(false, null);
            }
            case null, default ->
                    throw new RuntimeException("Received null or invalid game status update");

        }
        LOGGER.debug("Reconnection completed");

    }

    /**
     * Get the ExceptionThrower of the TuiUpdater
     *
     * @return the ExceptionThrower
     */
    @Override
    public @NotNull ExceptionThrower getExceptionThrower() {
        return exceptionReceiver;
    }

    /**
     * Get the ClientChatUpdater of the TuiUpdater
     *
     * @return the ClientChatUpdater
     */
    @Override
    public @NotNull ClientChatUpdater getChatUpdater() {
        return this;
    }


    /**
     * Set the current state of the TuiUpdater
     *
     * @param state the state to set
     */
    public void setTuiState(TuiStates state) {
        currentState.set(tuiStates.get(state));
    }

    /**
     * Check if the current state is the same as the state passed as argument
     *
     * @param state the state to check
     * @return true if the current state is the same as the state passed as argument
     */
    public boolean isCurrentState(TuiStates state) {
        return currentState.get() == tuiStates.get(state);
    }

    /**
     * Receive a message from the chat
     *
     * @param sender the nickname of the sender
     * @param msg    the message
     */
    @Override
    public void receiveMsg(@NotNull String sender, @NotNull String msg) {
        model.addChatMessage("[PUBLIC] " + sender + ": " + msg);
        if (isCurrentState(TuiStates.CHAT)) {
            currentState.get().restart(false, null);
        }
    }

    /**
     * Receive a private message from the chat
     *
     * @param sender the nickname of the sender
     * @param msg    the message
     */
    @Override
    public void receivePrivateMsg(@NotNull String sender, @NotNull String msg) {
        model.addChatMessage("[PRIVATE] " + sender + ": " + msg);
        if (isCurrentState(TuiStates.CHAT)) {
            currentState.get().restart(false, null);
        }
    }

    /**
     * Confirm that the message has been sent
     *
     * @param sender the nickname of the sender, it should be the same as the player's nickname
     * @param msg    the message
     */
    @Override
    public void confirmSentMsg(@NotNull String sender, @NotNull String msg) {
        if (sender.equals(model.myName())) {
            model.addChatMessage("[YOU] " + sender + ": " + msg);
        }
    }

    /**
     * Get the candidateNick, the candidateNick is the nickname that the player try to send to the
     * server
     *
     * @return the candidateNick
     */
    public String getCandidateNick() {
        return candidateNick;
    }

    /**
     * Set the candidateNick to the nickname passed as argument, the candidateNick is the nickname
     * that the player try to send to the server
     *
     * @param candidateNick the candidateNick to set
     */
    public void setCandidateNick(String candidateNick) {
        this.candidateNick = candidateNick;
    }

    /**
     * Get the current TuiState
     *
     * @return the current TuiState
     */
    public TUIState getCurrentTuiState() {
        return currentState.get();
    }

    /**
     * Get a specific instance of TUIState, the TUI states are all allocated during the reset of the
     * TuiUpdater
     *
     * @param state the state to get
     * @return the instance of the state
     */
    public TUIState getState(TuiStates state) {
        return tuiStates.get(state);
    }

    /**
     * Go back to the home state, the home state is the state that is considered default for a
     * specific phase of the game
     */
    public void goBack() {
        currentState.set(homeState.get());
        currentState.get().restart(false, null);
    }

    /**
     * Set the home state
     *
     * @param state the state to set as home state
     */
    public void setHomeState(TuiStates state) {
        homeState.set(tuiStates.get(state));
    }
}
