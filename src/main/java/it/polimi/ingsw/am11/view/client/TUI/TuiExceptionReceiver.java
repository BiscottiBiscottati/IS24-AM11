package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.view.client.ExceptionConnector;
import it.polimi.ingsw.am11.view.client.TUI.states.TuiStates;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TuiExceptionReceiver implements ExceptionConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(TuiUpdater.class);

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
        model.setCurrentTurn("Error in turns order");
        System.out.println("It's not your turn");
        System.out.println("MESSAGE: " + ex.getMessage());
    }

    @Override
    public void throwException(PlayerInitException ex) {
        if (tuiUpdater.isCurrentState(TuiStates.WAITING)) {
            //DONE
            tuiUpdater.setTuiState(TuiStates.SETTING_NAME);
            tuiUpdater.getCurrentTuiState().restart(true, ex);
            return;
        }
        //TODO


        // Should be unreachable
        System.out.println("PlayerInitException received");
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
        //DONE
        tuiUpdater.setTuiState(TuiStates.SETTING_NUM);
        tuiUpdater.getCurrentTuiState().restart(true, ex);
    }

    @Override
    public void throwException(NotGodPlayerException ex) {
        //DONE
        model.setGodPlayer(null);
        tuiUpdater.setTuiState(TuiStates.SETTING_NAME);
        tuiUpdater.getCurrentTuiState().restart(true, ex);
    }

    @Override
    public void throwException(GameStatusException ex) {
        LOGGER.debug("GameStatusException received: {}" + ex.getMessage());
        System.out.println(ex.getMessage());
    }

    @Override
    public void throwException(NotSetNumOfPlayerException ex) {
        LOGGER.debug("NotSetNumOfPlayersException received: {}" + ex.getMessage());
        if (tuiUpdater.isCurrentState(TuiStates.WAITING)) {
            //DONE
            tuiUpdater.setCandidateNick(null);
            tuiUpdater.getCurrentTuiState().restart(true, ex);
            return;
        }


        // Should be unreachable
        System.out.println("NotSetNumOfPlayersException received: ");
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
