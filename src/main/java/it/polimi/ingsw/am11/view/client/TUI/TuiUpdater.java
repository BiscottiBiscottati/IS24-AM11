package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import it.polimi.ingsw.am11.view.client.ExceptionConnector;
import it.polimi.ingsw.am11.view.client.TUI.printers.CardPrinter;
import it.polimi.ingsw.am11.view.client.TUI.states.TUIState;
import it.polimi.ingsw.am11.view.client.TUI.states.TuiStates;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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

    public TuiUpdater(MiniGameModel model, TuiStates startingState) {
        this.model = model;
        this.tuiStates = new EnumMap<>(TuiStates.class);
        for (TuiStates state : TuiStates.values()) {
            tuiStates.put(state, state.getNewState());
        }
        this.currentState = new AtomicReference<>(tuiStates.get(TuiStates.CONNECTING));
        this.exceptionReceiver = new TuiExceptionReceiver(model, this);
    }

    @Override
    public void updateDeckTop(@NotNull PlayableCardType type, @NotNull Color color) {
        LOGGER.debug(model.getCurrentTurn() + "picked a card from the " + type.getName() +
                     " deck, the " + type.getName() + " deck top card is now " +
                     color.getColumnName());

        model.table().refreshDeckTop(type, color);
    }

    @Override
    public void updateField(String nickname, int x, int y, int cardId,
                            boolean isRetro, boolean removeMode) {
        //DONE
        Position pos = new Position(x, y);
        if (! removeMode) {
            model.getCliPlayer(nickname).getField().place(pos, cardId, isRetro);
        } else {
            model.getCliPlayer(nickname).getField().remove(pos);
        }

        if (isCurrentState(TuiStates.PLACING)) {
            //TODO sucsefull placement
        }


    }

    @Override
    public void updateShownPlayable(Integer previousId, Integer currentId) {
        if (previousId != null) model.table().pickVisible(previousId);
        if (currentId != null) model.table().addVisible(currentId);
        LOGGER.debug("Removed from visible: {}, Added: {}", previousId, currentId);
    }

    @Override
    public void updateTurnChange(String nickname) {
        //FIXME

        model.setCurrentTurn(nickname);
        LOGGER.debug("It's {} turn", nickname);
    }

    @Override
    public void updatePlayerPoint(String nickname, int points) {
        //DONE
        model.getCliPlayer(nickname).addPoints(points);

        if (isCurrentState(TuiStates.WAITING_FOR_TURN)) {
            //TODO update the points on the screen
        }
    }

    @Override
    public void updateGameStatus(GameStatus status) {
        model.table().setStatus(status);
        LOGGER.debug("Game status event:" + status);
        switch (status) {
            case SETUP -> {
            }
            case CHOOSING_STARTERS -> {
                //DONE
                currentState.set(tuiStates.get(TuiStates.CHOOSING_STARTER));
                currentState.get().restart(false, null);
                try {
                    CardPrinter.printCardFrontAndBack(
                            model.getCliPlayer(model.myName()).getSpace().getStarterCard());
                } catch (IllegalCardBuildException e) {
                    throw new RuntimeException(e);
                }
                System.out.print("Place it on its front or on its retro >>> \033[K");
            }
            case CHOOSING_OBJECTIVES -> {
                currentState.set(tuiStates.get(TuiStates.CHOOSING_OBJECTIVE));
                currentState.get().restart(false, null);
                try {
                    CardPrinter.printObjectives(new ArrayList<>(model.getCliPlayer(
                            model.myName()).getSpace().getCandidateObjectives()));
                } catch (IllegalCardBuildException e) {
                    throw new RuntimeException(e);
                }
                System.out.print("Choose one of the objectives above >>> \033[K");
            }
            case ENDED -> {
                currentState.set(tuiStates.get(TuiStates.ENDED));
                currentState.get().restart(false, null);
            }
            case ONGOING -> {
                //FIXME
                currentState.set(tuiStates.get(TuiStates.WAITING_FOR_TURN));
                currentState.get().restart(false, null);

            }
            case ARMAGEDDON -> {
                System.out.println("The final phase of the game has began, the next turn will be " +
                                   "the last one, choose wisely your moves");
            }
            case LAST_TURN -> {
                System.out.println("This is the last turn, play the ace up your sleeve");
            }
            case null, default -> {
                System.out.println("idk what's going on");
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
        //DONE
        if (removeMode) {
            model.removeCardFromHand(cardId);
        } else {
            model.addCardInHand(cardId);
            model.setiPlaced(false);
        }

        if (isCurrentState(TuiStates.WAITING_FOR_TURN)) {
            //TODO update the hand on the screen
        }
        //TODO update also other cases

    }

    @Override
    public void updatePersonalObjective(int cardId, boolean removeMode) {

        if (removeMode) {
            model.rmPersonalObjective(cardId);
        } else {
            model.addPersonalObjective(cardId);
        }
    }

    @Override
    public void receiveStarterCard(int cardId) {
        //DONE
        model.addStarterCard(cardId);
        LOGGER.debug("Receive starter event, card id: {}", cardId);
    }

    @Override
    public void receiveCandidateObjective(Set<Integer> cardId) {
        //DONE
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
        //DONE
        model.setMyName(candidateNick);
        for (Map.Entry<PlayerColor, String> entry : currentPlayers.entrySet()) {
            model.addPlayer(entry.getValue(), entry.getKey());
        }
    }

    @Override
    public void updateNumOfPlayers(int numOfPlayers) {
        if (isCurrentState(TuiStates.WAITING) && ! model.getGodPlayer().equals(model.myName())) {
            currentState.set(tuiStates.get(TuiStates.SETTING_NAME));
            currentState.get().restart(false, null);
        }
    }

    @Override
    public ExceptionConnector getExceptionConnector() {
        return exceptionReceiver;
    }

    public boolean isCurrentState(TuiStates state) {
        return currentState.equals(tuiStates.get(state));
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

    public void setTuiState(TuiStates state) {
        currentState.set(tuiStates.get(state));
    }

    public TUIState getState(TuiStates state) {
        return tuiStates.get(state);
    }

}
