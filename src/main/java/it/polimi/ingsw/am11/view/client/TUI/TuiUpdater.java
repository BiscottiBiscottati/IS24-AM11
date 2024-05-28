package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import it.polimi.ingsw.am11.view.client.ExceptionConnector;
import it.polimi.ingsw.am11.view.client.TUI.states.TUIState;
import it.polimi.ingsw.am11.view.client.TUI.states.TuiStates;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

// This class is the implementation of the ClientViewUpdater and the ExceptionConnector.
// The classes that handle the interpretation of the messages from the net will call these methods
// There are also methods designed to be used by the Actuator ( and possibly other classes) to
// update and get the TUIState and to save the candidateNick (the nickname that the player try to
// send to the server)

public class TuiUpdater implements ClientViewUpdater {
    private static final Logger LOGGER = LoggerFactory.getLogger(TuiUpdater.class);

    private final MiniGameModel model;
    private final EnumMap<TuiStates, TUIState> tuiStates;
    private final TuiExceptionReceiver exceptionReceiver;
    private final AtomicReference<TUIState> currentState;
    private String candidateNick = "";

    public TuiUpdater(@NotNull MiniGameModel model, TuiStates startingState) {
        this.model = model;
        this.tuiStates = new EnumMap<>(TuiStates.class);
        for (TuiStates state : TuiStates.values()) {
            tuiStates.put(state, state.getNewState(model));
        }
        this.currentState = new AtomicReference<>(tuiStates.get(startingState));
        this.exceptionReceiver = new TuiExceptionReceiver(model, this);
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
    public void updateField(String nickname, int x, int y, int cardId,
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
            System.out.println("check 3");
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
            currentState.get().restart(false, null);
        }
    }

    @Override
    public void updatePlayerPoint(String nickname, int points) {
        model.getCliPlayer(nickname).addPoints(points);
        LOGGER.debug("{} points added to {}", points, nickname);
        if (isCurrentState(TuiStates.WATCHING_TABLE)) {
            currentState.get().restart(false, null);
        }
    }

    @Override
    public void updateGameStatus(GameStatus status) {
        model.table().setStatus(status);
        LOGGER.debug("Game status event: {}", status);
        switch (status) {
            case SETUP, ARMAGEDDON, LAST_TURN -> {
            }
            case CHOOSING_STARTERS -> {
                currentState.set(tuiStates.get(TuiStates.CHOOSING_STARTER));
                currentState.get().restart(false, null);
            }
            case CHOOSING_OBJECTIVES -> {
                currentState.set(tuiStates.get(TuiStates.CHOOSING_OBJECTIVE));
                currentState.get().restart(false, null);
            }
            case ENDED -> {
                currentState.set(tuiStates.get(TuiStates.ENDED));
                currentState.get().restart(false, null);
            }
            case ONGOING -> {
                if (model.getCurrentTurn().equals(model.myName())) {
                    setTuiState(TuiStates.WATCHING_FIELD);
                    currentState.get().restart(false, null);
                } else {
                    currentState.set(tuiStates.get(TuiStates.WATCHING_TABLE));
                    currentState.get().restart(false, null);
                }
            }
            case null, default -> {
                throw new RuntimeException("Received null or invalid game status update");
            }
        }
    }

    @Override
    public void updateCommonObjective(Set<Integer> cardId, boolean removeMode) {
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
    public void receiveFinalLeaderboard(Map<String, Integer> finalLeaderboard) {
        //TODO
        model.setFinalLeaderboard(finalLeaderboard);
        System.out.println("The final leaderboard is:");
        for (int i = 1; i <= finalLeaderboard.size(); i++) {
            for (String name : finalLeaderboard.keySet()) {
                if (finalLeaderboard.get(name) == i) {
                    System.out.println(i + ". " + name);
                }
            }
        }
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
        currentState.get().restart(false, null);
    }

    @Override
    public void updatePlayers(Map<PlayerColor, String> currentPlayers) {
        model.setMyName(candidateNick);
        for (Map.Entry<PlayerColor, String> entry : currentPlayers.entrySet()) {
            model.addPlayer(entry.getValue(), entry.getKey());
        }
    }

    @Override
    public void updateNumOfPlayers(int numOfPlayers) {

    }

    @Override
    public void disconnectedFromServer() {
        //TODO to implement exit tui or reset to try to reconnect
    }

    @Override
    public ExceptionConnector getExceptionConnector() {
        return exceptionReceiver;
    }

    public void setTuiState(TuiStates state) {
        currentState.set(tuiStates.get(state));
    }

    public boolean isCurrentState(TuiStates state) {
        return currentState.get() == tuiStates.get(state);
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

}
