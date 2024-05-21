package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import it.polimi.ingsw.am11.view.client.ExceptionConnector;
import it.polimi.ingsw.am11.view.client.TUI.printers.CardPrinter;
import it.polimi.ingsw.am11.view.client.TUI.states.TUIState;
import it.polimi.ingsw.am11.view.client.TUI.states.TuiStates;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

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
    private TUIState currentState;
    private String candidateNick = "";

    public TuiUpdater(MiniGameModel model, TuiStates startingState) {
        this.model = model;
        this.tuiStates = new EnumMap<>(TuiStates.class);
        for (TuiStates state : TuiStates.values()) {
            tuiStates.put(state, state.getNewState());
        }
        this.currentState = tuiStates.get(TuiStates.CONNECTING);
        this.exceptionReceiver = new TuiExceptionReceiver(model, this);
    }

    @Override
    public void updateDeckTop(PlayableCardType type, Color color) {
        //TODO
        model.table().refreshDeckTop(type, color);
        if (model.getCurrentTurn().equals(model.myName())) {
            System.out.println("You picked a card from the " + type.getName() +
                               " deck, the " + type.getName() + " deck top card is now " +
                               color.getColumnName());
        } else {
            System.out.println(
                    model.getCurrentTurn() + "picked a card from the " + type.getName() +
                    " deck, the " + type.getName() + " deck top card is now " +
                    color.getColumnName());
        }
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
    public void updateShownPlayable(int previousId, int currentId) {
        //TODO
        model.table().pickVisible(previousId);
        model.table().addVisible(currentId);
        if (model.getCurrentTurn().isEmpty()) {
            System.out.println("Added visible card to the table: " + currentId);
            return;
        }
        if (model.getCurrentTurn().equals(model.myName())) {
            System.out.println("You picked " + previousId + " from the visible " +
                               "cards, it has been replaced by " + currentId);
        } else {
            System.out.println(
                    model.getCurrentTurn() + "picked " + previousId + " from the visible " +
                    "cards, it has been replaced by " + currentId);
        }

    }

    @Override
    public void updateTurnChange(String nickname) {
        //TODO
        model.setCurrentTurn(nickname);
        if (nickname.equals(model.myName())) {
            System.out.println("It's now your turn, good luck");
            currentState = tuiStates.get(TuiStates.PLACING);
            System.out.println("TUI STATUS: " + TuiStates.PLACING);
        } else {
            System.out.println("It's now " + nickname + " turn");
            currentState = tuiStates.get(TuiStates.WAITING);
            System.out.println("TUI STATUS: " + TuiStates.WAITING);
        }
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
                currentState = tuiStates.get(TuiStates.CHOOSING_STARTER);
                currentState.restart(false, null);
                try {
                    CardPrinter.printCardFrontAndBack(
                            model.getCliPlayer(model.myName()).getSpace().getStarterCard());
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
                System.out.print("Place it on its front or on its back >>> ");
            }
            case CHOOSING_OBJECTIVES -> {
                currentState = tuiStates.get(TuiStates.CHOOSING_OBJECTIVE);
                currentState.restart(false, null);
                try {
                    CardPrinter.printObjectives(new ArrayList<>(model.getCliPlayer(
                            model.myName()).getSpace().getCandidateObjectives()));
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
                System.out.print("Choose one of the objectives above >>> ");
            }
            case ENDED -> {
                currentState = tuiStates.get(TuiStates.ENDED);
                currentState.restart(false, null);
            }
            case ONGOING -> {
                System.out.println("The game has began, fight with honor");
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
        //TODO
        if (removeMode) {
            cardId.stream().forEach(x -> model.table().removeCommonObjective(x));
            cardId.stream().forEach(x -> System.out.println("The card: " + x + " is no longer a " +
                                                            "common objective"));
        } else {
            cardId.stream().forEach(x -> model.table().addCommonObjectives(x));
            cardId.stream().forEach(x -> System.out.println("New common objective added to the " +
                                                            "table: " + x));
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

        model.getCliPlayer(model.myName()).getSpace().addCandidateObjectives(cardId);
        cardId.stream().forEach(
                x -> LOGGER.debug("Receive candidate objective event, card id: {}", x));
    }

    @Override
    public void notifyGodPlayer() {
        LOGGER.debug("EVENT: God player notification");
        model.setMyName(candidateNick);
        model.setGodPlayer(candidateNick);
        currentState = tuiStates.get(TuiStates.SETTING_NUM);
        currentState.restart(false, null);
    }

    @Override
    public void updatePlayers(Map<PlayerColor, String> currentPlayers) {
        //DONE
        model.setMyName(candidateNick);
        for (PlayerColor pc : currentPlayers.keySet()) {
            model.addPlayer(currentPlayers.get(pc), pc);
        }
    }

    @Override
    public void updateNumOfPlayers(int numOfPlayers) {
        if (isCurrentState(TuiStates.WAITING) && ! model.getGodPlayer().equals(model.myName())) {
            currentState = tuiStates.get(TuiStates.SETTING_NAME);
            currentState.restart(false, null);
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
        return currentState;
    }

    public void setTuiState(TuiStates state) {
        currentState = tuiStates.get(state);
    }

    public TUIState getState(TuiStates state) {
        return tuiStates.get(state);
    }

}
