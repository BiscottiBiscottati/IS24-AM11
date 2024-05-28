package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
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
        LOGGER.debug("IllegalPlayerSpaceActionException {}", ex.getMessage());
        //CASE: trying to set objective that is not yours
        if (tuiUpdater.isCurrentState(TuiStates.WAITING)) {
            tuiUpdater.setTuiState(TuiStates.CHOOSING_OBJECTIVE);
            tuiUpdater.getCurrentTuiState().restart(true, ex);
            return;
        }

    }

    @Override
    public void throwException(TurnsOrderException ex) {
        LOGGER.debug("TurnsOrderException {}", ex.getMessage());
        //FIXME
    }

    @Override
    public void throwException(PlayerInitException ex) {
        LOGGER.debug("PlayerInitException {}", ex.getMessage());
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
        LOGGER.debug("IllegalCardPlacingException {}", ex.getMessage());
        model.setiPlaced(false);
        tuiUpdater.setTuiState(TuiStates.WATCHING_FIELD);
        tuiUpdater.getCurrentTuiState().restart(true, ex);
    }

    @Override
    public void throwException(IllegalPickActionException ex) {
        LOGGER.debug("IllegalPickActionException {}", ex.getMessage());
        tuiUpdater.setTuiState(TuiStates.WATCHING_FIELD);
        tuiUpdater.getCurrentTuiState().restart(true, ex);
    }

    @Override
    public void throwException(NotInHandException ex) {
        LOGGER.debug("NotInHandException {}", ex.getMessage());
        model.setiPlaced(false);
        tuiUpdater.setTuiState(TuiStates.WATCHING_FIELD);
        tuiUpdater.getCurrentTuiState().restart(true, ex);
    }

    @Override
    public void throwException(EmptyDeckException ex) {
        LOGGER.debug("EmptyDeckException {}", ex.getMessage());
        tuiUpdater.setTuiState(TuiStates.WATCHING_FIELD);
        tuiUpdater.getCurrentTuiState().restart(true, ex);
    }

    @Override
    public void throwException(NumOfPlayersException ex) {
        LOGGER.debug("NumOfPlayersException {}", ex.getMessage());
        //DONE
        tuiUpdater.setTuiState(TuiStates.SETTING_NUM);
        tuiUpdater.getCurrentTuiState().restart(true, ex);
    }

    @Override
    public void throwException(NotGodPlayerException ex) {
        LOGGER.debug("NotGodPlayerException {}", ex.getMessage());
        //DONE
        model.setGodPlayer(null);
        tuiUpdater.setTuiState(TuiStates.SETTING_NAME);
        tuiUpdater.getCurrentTuiState().restart(true, ex);
    }

    @Override
    public void throwException(GameStatusException ex) {
        LOGGER.debug("GameStatusException received: {}" + ex.getMessage());
        //TODO
    }

    @Override
    public void throwException(NotSetNumOfPlayerException ex) {
        LOGGER.debug("NotSetNumOfPlayerException received: {}" + ex.getMessage());
        if (tuiUpdater.isCurrentState(TuiStates.WAITING)) {
            tuiUpdater.setCandidateNick("");
            tuiUpdater.setTuiState(TuiStates.SETTING_NAME);
            tuiUpdater.getCurrentTuiState().restart(true, ex);
            return;
        }


        // Should be unreachable
        System.out.println("NotSetNumOfPlayersException received: ");
        System.out.println("MESSAGE: " + ex.getMessage());
    }

    @Override
    public void throwException(IllegalPlateauActionException ex) {
        LOGGER.debug("IllegalPlateauActionException {}", ex.getMessage());
        //TODO
    }

    @Override
    public void throwException(MaxHandSizeException ex) {
        LOGGER.debug("MaxHandSizeException {}", ex.getMessage());
        //TODO
    }

    @Override
    public void throwException(LostConnectionException ex) {
        LOGGER.debug("LostConnectionException {}", ex.getMessage());
        //TODO
    }
}
