package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.model.utils.TurnAction;
import it.polimi.ingsw.am11.model.utils.memento.PlayerMemento;
import it.polimi.ingsw.am11.model.utils.memento.ReconnectionModelMemento;
import it.polimi.ingsw.am11.view.client.ClientChatUpdater;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import it.polimi.ingsw.am11.view.client.ExceptionThrower;
import it.polimi.ingsw.am11.view.client.TUI.states.TUIState;
import it.polimi.ingsw.am11.view.client.TUI.states.TuiStates;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;
import java.util.SequencedMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

// This class is the implementation of the ClientViewUpdater and the ExceptionConnector.
// The classes that handle the interpretation of the messages from the net will call these methods
// There are also methods designed to be used by the Actuator ( and possibly other classes) to
// update and get the TUIState and to save the candidateNick (the nickname that the player try to
// send to the server)

public class TuiUpdater implements ClientViewUpdater, ClientChatUpdater {
    private static final Logger LOGGER = LoggerFactory.getLogger(TuiUpdater.class);
    private final EnumMap<TuiStates, TUIState> tuiStates;
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

    public void reset(TuiStates startingState) {
        this.model = new MiniGameModel();
        for (TuiStates state : TuiStates.values()) {
            tuiStates.put(state, state.getNewState(model));
        }
        this.currentState = new AtomicReference<>(tuiStates.get(startingState));
        this.exceptionReceiver = new TuiExceptionReceiver(model, this);
        this.homeState = new AtomicReference<>();
    }

    @Override
    public void updateDeckTop(@NotNull PlayableCardType type, @NotNull Color color) {
        LOGGER.debug("{} picked a card from the {} deck, the {} deck top card is now {}",
                     model.getCurrentTurn(), type.getName(), type.getName(), color.getColumnName());

        model.table().refreshDeckTop(type, color);

        if (isCurrentState(TuiStates.WATCHING_TABLE)) {
            currentState.get().restart(false, null);
        }
    }

    @Override
    public void updateField(@NotNull String nickname, int x, int y, int cardId,
                            boolean isRetro, boolean removeMode) {
        LOGGER.debug("updateField: Nickname: {}, X: {}, Y: {}, cardId: {}, isRetro: {}, " +
                     "removemode: {}", nickname, x, y, cardId, isRetro, removeMode);
        Position pos = new Position(x, y);
        if (! removeMode) {
            model.getCliPlayer(nickname).getField().place(pos, cardId, isRetro);
        } else {
            model.getCliPlayer(nickname).getField().remove(pos);
        }

        if (isCurrentState(TuiStates.WATCHING_FIELD)) {
            String[] args = {"notify", nickname};
            currentState.get().passArgs(null, args);
        }


    }

    @Override
    public void updateShownPlayable(Integer previousId, Integer currentId) {
        if (previousId != null) model.table().pickVisible(previousId);
        if (currentId != null) model.table().addVisible(currentId);
        LOGGER.debug("Removed from visible: {}, Added: {}", previousId, currentId);

        if (isCurrentState(TuiStates.WATCHING_TABLE)) {
            currentState.get().restart(false, null);
        }
    }

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

    @Override
    public void updatePlayerPoint(@NotNull String nickname, int points) {
        model.getCliPlayer(nickname).addPoints(points);
        LOGGER.debug("{} points added to {}", points, nickname);
        if (isCurrentState(TuiStates.WATCHING_TABLE)) {
            currentState.get().restart(false, null);
        }
    }

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
            case null, default -> {
                throw new RuntimeException("Received null or invalid game status update");
            }
        }
    }

    @Override
    public void updateCommonObjective(@NotNull Set<Integer> cardId, boolean removeMode) {
        if (removeMode) {
            cardId.stream().forEach(x -> model.table().removeCommonObjective(x));
            LOGGER.debug("CommonObjRm: {}", cardId);
        } else {
            cardId.stream().forEach(x -> model.table().addCommonObjectives(x));
            LOGGER.debug("CommonObjAdd: {}", cardId);
        }

        if (isCurrentState(TuiStates.WATCHING_TABLE)) {
            currentState.get().restart(false, null);
        }
    }

    @Override
    public void receiveFinalLeaderboard(@NotNull Map<String, Integer> finalLeaderboard) {
        model.setFinalLeaderboard(finalLeaderboard);
        setTuiState(TuiStates.ENDED);
        homeState.set(tuiStates.get(TuiStates.ENDED));
        currentState.get().restart(false, null);
    }

    @Override
    public void updateHand(int cardId, boolean removeMode) {
        LOGGER.debug("UpdateHand, cardId: {}, removeMode: {}", cardId, removeMode);
        if (removeMode) {
            model.removeCardFromHand(cardId);
        } else {
            model.addCardInHand(cardId);
            model.setiPlaced(false);
        }

        if (isCurrentState(TuiStates.WATCHING_TABLE)) {
            currentState.get().restart(false, null);
        } else if (isCurrentState(TuiStates.WATCHING_FIELD)) {
            String[] args = {"notify", model.myName()};
            currentState.get().passArgs(null, args);
        }
    }

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

    @Override
    public void receiveStarterCard(int cardId) {
        model.addStarterCard(cardId);
        LOGGER.debug("Receive starter event, card id: {}", cardId);
    }

    @Override
    public void receiveCandidateObjective(Set<Integer> cardId) {
        model.getCliPlayer(model.myName()).getSpace().addCandidateObjectives(cardId);
        cardId.forEach(
                x -> LOGGER.debug("Receive candidate objective event, card id: {}", x));
    }

    @Override
    public void notifyGodPlayer() {
        LOGGER.debug("EVENT: God player notification");
        model.setMyName(candidateNick);
        model.setGodPlayer(candidateNick);
        currentState.set(tuiStates.get(TuiStates.SETTING_NUM));
        homeState.set(tuiStates.get(TuiStates.SETTING_NUM));
        currentState.get().restart(false, null);
    }

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

    @Override
    public void updateNumOfPlayers(int numOfPlayers) {

    }

    @Override
    public void disconnectedFromServer(@NotNull String message) {
        LOGGER.error("Disconnected from server: {}", message);
        reset(TuiStates.CONNECTING);
        //FIXME
        currentState.get().restart(false, null);

    }

    @Override
    public void receiveReconnection(@NotNull ReconnectionModelMemento memento) {
        model.setMyName(candidateNick);
        model.setStartingPlayer(memento.playerManager().firstPlayer());
        model.setCurrentTurn(memento.playerManager().currentPlayer());

        //table
        for (PlayableCardType deckTop : memento.table().deckTops().keySet()) {
            model.table().refreshDeckTop(deckTop,
                                         memento.table().deckTops().get(deckTop));
        }
        for (PlayableCardType shown : memento.table().shownPlayable().keySet()) {
            memento.table().shownPlayable().get(shown).forEach(x -> model.table().addVisible(x));
        }
        for (Integer commonObj : memento.table().commonObjs()) {
            model.table().addCommonObjectives(commonObj);
        }

        //players manager
        for (PlayerMemento player : memento.playerManager().players()) {
            model.addPlayer(player.nickname(), player.color());
            if (model.myName().equals(player.nickname())) {
                player.space().hand().forEach(x -> model.addCardInHand(x));
                player.space().personalObjs().forEach(x -> model.addPersonalObjective(x));
                model.getCliPlayer(model.myName()).getSpace().addCandidateObjectives(
                        player.space().candidateObjs());
                model.getCliPlayer(model.myName()).getSpace().setStarterCard(
                        player.space().starterCard());
                model.setiPlaced(
                        memento.playerManager().currentAction().equals(TurnAction.DRAW_CARD));
            }
            player.field().positionManager().cardPositioned().forEach(
                    (pos, cardContainerMemento) -> {
                        model.absolutePlace(player.nickname(), pos, cardContainerMemento.card(),
                                            cardContainerMemento.isRetro(),
                                            cardContainerMemento.coveredCorners());
                    });
        }

        //Plateau
        if (! memento.plateau().leaderboard().isEmpty()) {
            model.setFinalLeaderboard(memento.plateau().leaderboard());
        }
        model.table().setStatus(memento.plateau().status());
        memento.plateau().playerPoints().forEach(
                (nickname, points) -> model.getCliPlayer(nickname).addPoints(points));


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
            case null, default -> {
                throw new RuntimeException("Received null or invalid game status update");
            }

        }

    }

    @Override
    public @NotNull ExceptionThrower getExceptionThrower() {
        return exceptionReceiver;
    }

    @Override
    public @NotNull ClientChatUpdater getChatUpdater() {
        return this;
    }

    public void setTuiState(TuiStates state) {
        currentState.set(tuiStates.get(state));
    }

    public boolean isCurrentState(TuiStates state) {
        return currentState.get() == tuiStates.get(state);
    }

    @Override
    public void receiveMsg(@NotNull String sender, @NotNull String msg) {
        model.addChatMessage("[PUBLIC] " + sender + ": " + msg);
        if (isCurrentState(TuiStates.CHAT)) {
            currentState.get().restart(false, null);
        }
    }

    @Override
    public void receivePrivateMsg(@NotNull String sender, @NotNull String msg) {
        model.addChatMessage("[PRIVATE] " + sender + ": " + msg);
        if (isCurrentState(TuiStates.CHAT)) {
            currentState.get().restart(false, null);
        }
    }

    @Override
    public void confirmSentMsg(@NotNull String sender, @NotNull String msg) {
        if (sender.equals(model.myName())) {
            model.addChatMessage("[YOU] " + sender + ": " + msg);
        }
    }

    public String getCandidateNick() {
        return candidateNick;
    }

    public void setCandidateNick(String candidateNick) {
        this.candidateNick = candidateNick;
    }

    public TUIState getCurrentTuiState() {
        return currentState.get();
    }

    public TUIState getState(TuiStates state) {
        return tuiStates.get(state);
    }

    public void goBack() {
        currentState.set(homeState.get());
        currentState.get().restart(false, null);
    }

    public void setHomeState(TuiStates state) {
        homeState.set(tuiStates.get(state));
    }
}
