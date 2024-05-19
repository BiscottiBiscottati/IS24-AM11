package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.view.client.ExceptionConnector;
import it.polimi.ingsw.am11.view.client.TUI.states.TuiStates;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;

public class TuiExceptionReceiver implements ExceptionConnector {

    private final MiniGameModel model;
    private final TuiUpdater tuiUpdater;


    public TuiExceptionReceiver(MiniGameModel model, TuiUpdater tuiUpdater) {
        this.model = model;
        this.tuiUpdater = tuiUpdater;
    }


    @Override
    public void throwException(IllegalPlayerSpaceActionException ex) {
        System.out.println("IllegalPlayerSpaceActionException");
        System.out.println("MESSAGE: " + ex.getMessage());
    }

    @Override
    public void throwException(TurnsOrderException ex) {
        model.setCurrentTurn(null);
        System.out.println("It's not your turn");
        System.out.println("MESSAGE: " + ex.getMessage());
    }

    @Override
    public void throwException(PlayerInitException ex) {
        System.out.println("Something went wrong while trying to join a new game");
        System.out.println("MESSAGE: " + ex.getMessage());
    }

    @Override
    public void throwException(IllegalCardPlacingException ex) {
        System.out.println("You can't place that card there");
        System.out.println("MESSAGE: " + ex.getMessage());
    }

    @Override
    public void throwException(IllegalPickActionException ex) {
        System.out.println("You can't do that");
        System.out.println("MESSAGE: " + ex.getMessage());
    }

    @Override
    public void throwException(NotInHandException ex) {
        System.out.println("The card you chose is not in your hand");
        System.out.println("MESSAGE: " + ex.getMessage());
    }

    @Override
    public void throwException(EmptyDeckException ex) {
        System.out.println("The deck you chose is empty");
        System.out.println("MESSAGE: " + ex.getMessage());
    }

    @Override
    public void throwException(NumOfPlayersException ex) {
        tuiUpdater.setTuiState(TuiStates.SETTING_NUM);
        System.out.println("The number of players you chose is not valid");
        System.out.println("MESSAGE: " + ex.getMessage());
    }

    @Override
    public void throwException(NotGodPlayerException ex) {
        model.setGodPlayer(null);
        tuiUpdater.setTuiState(TuiStates.SETTING_NAME);
        System.out.println("Your are not the moderator, wait for the moderator");
        System.out.println("MESSAGE: " + ex.getMessage());
    }

    @Override
    public void throwException(GameStatusException ex) {
        System.out.println(ex.getMessage());
    }

    @Override
    public void throwException(NotSetNumOfPlayerException ex) {
        tuiUpdater.setCandidateNick(null);
        tuiUpdater.setTuiState(TuiStates.SETTING_NAME);
        System.out.println("Please wait until the moderator set the number of players for this " +
                           "match");
        System.out.println("MESSAGE: " + ex.getMessage());
    }

    @Override
    public void throwException(IllegalPlateauActionException description) {
        //TODO
    }

    @Override
    public void throwException(MaxHandSizeException description) {
        //TODO
    }

    @Override
    public void throwException(LostConnectionException description) {
        //TODO
    }
}
