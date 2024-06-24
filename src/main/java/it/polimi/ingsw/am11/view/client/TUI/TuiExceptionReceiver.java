package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.view.client.ExceptionThrower;
import it.polimi.ingsw.am11.view.client.TUI.states.TuiStates;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TuiExceptionReceiver implements ExceptionThrower {
    private static final Logger LOGGER = LoggerFactory.getLogger(TuiExceptionReceiver.class);

    private final MiniGameModel model;
    private final TuiUpdater tuiUpdater;


    public TuiExceptionReceiver(MiniGameModel model, TuiUpdater tuiUpdater) {
        this.model = model;
        this.tuiUpdater = tuiUpdater;
    }

    /**
     * Handles the IllegalPlayerSpaceActionException, thrown when a player tries to perform an
     * action on a space that is not allowed
     *
     * @param ex the exception thrown
     */
    @Override
    public void throwException(@NotNull IllegalPlayerSpaceActionException ex) {
        LOGGER.debug("IllegalPlayerSpaceActionException {}", ex.getMessage());
        //CASE: trying to set objective that is not yours
        if (tuiUpdater.isCurrentState(TuiStates.WAITING)) {
            tuiUpdater.setTuiState(TuiStates.CHOOSING_OBJECTIVE);
            tuiUpdater.setHomeState(TuiStates.CHOOSING_OBJECTIVE);
            tuiUpdater.getCurrentTuiState().restart(true, ex);
        }

    }

    /**
     * Handles the TurnsOrderException, thrown when the player tries to perform an action while it's
     * not his turn
     *
     * @param ex the exception thrown
     */
    @Override
    public void throwException(@NotNull TurnsOrderException ex) {
        LOGGER.debug("TurnsOrderException {}", ex.getMessage());
        throw new RuntimeException(ex);
    }

    /**
     * Handles the PlayerInitException, thrown when the player tries to initialize the game with an
     * invalid number of players
     *
     * @param ex the exception thrown
     */
    @Override
    public void throwException(@NotNull PlayerInitException ex) {
        LOGGER.debug("PlayerInitException {}", ex.getMessage());
        if (tuiUpdater.isCurrentState(TuiStates.WAITING)) {
            tuiUpdater.setCandidateNick("");
            tuiUpdater.setTuiState(TuiStates.SETTING_NAME);
            tuiUpdater.setHomeState(TuiStates.SETTING_NAME);
            tuiUpdater.getCurrentTuiState().restart(true, ex);
        }

    }

    /**
     * Handles the IllegalCardPlacingException, thrown when the player tries to place a card in an
     * illegal position
     *
     * @param ex the exception thrown
     */
    @Override
    public void throwException(@NotNull IllegalCardPlacingException ex) {
        LOGGER.debug("IllegalCardPlacingException {}", ex.getMessage());
        model.setiPlaced(false);
        if (model.table().getStatus().equals(GameStatus.ONGOING) ||
            model.table().getStatus().equals(GameStatus.ARMAGEDDON) ||
            model.table().getStatus().equals(GameStatus.LAST_TURN)) {
            tuiUpdater.setTuiState(TuiStates.WATCHING_FIELD);
            tuiUpdater.setHomeState(TuiStates.WATCHING_FIELD);
            tuiUpdater.getCurrentTuiState().restart(true, ex);
        }
    }

    /**
     * Handles the IllegalPickActionException, thrown when the player tries to pick that is not
     * allowed
     *
     * @param ex the exception thrown
     */
    @Override
    public void throwException(@NotNull IllegalPickActionException ex) {
        LOGGER.debug("IllegalPickActionException {}", ex.getMessage());
        tuiUpdater.setTuiState(TuiStates.WATCHING_TABLE);
        tuiUpdater.setHomeState(TuiStates.WATCHING_TABLE);
        tuiUpdater.getCurrentTuiState().restart(true, ex);
    }

    /**
     * Handles the NotInHandException, thrown when the player tries to use a card that is not in his
     * hand
     *
     * @param ex the exception thrown
     */
    @Override
    public void throwException(@NotNull NotInHandException ex) {
        LOGGER.debug("NotInHandException {}", ex.getMessage());
        model.setiPlaced(false);
        tuiUpdater.setTuiState(TuiStates.WATCHING_FIELD);
        tuiUpdater.setHomeState(TuiStates.WATCHING_FIELD);
        tuiUpdater.getCurrentTuiState().restart(true, ex);
    }

    /**
     * Handles the EmptyDeckException, thrown when the player tries to draw a card from an empty
     * deck
     *
     * @param ex the exception thrown
     */
    @Override
    public void throwException(@NotNull EmptyDeckException ex) {
        LOGGER.debug("EmptyDeckException {}", ex.getMessage());
        tuiUpdater.setTuiState(TuiStates.WATCHING_FIELD);
        tuiUpdater.setHomeState(TuiStates.WATCHING_FIELD);
        tuiUpdater.getCurrentTuiState().restart(true, ex);
    }

    /**
     * Handles the NumOfPlayersException, thrown when the player tries to set an invalid number of
     * players
     *
     * @param ex the exception thrown
     */
    @Override
    public void throwException(@NotNull NumOfPlayersException ex) {
        LOGGER.debug("NumOfPlayersException {}", ex.getMessage());
        if (model.getGodPlayer().equals(model.myName())) {
            tuiUpdater.setTuiState(TuiStates.SETTING_NUM);
            tuiUpdater.setHomeState(TuiStates.SETTING_NUM);
            tuiUpdater.getCurrentTuiState().restart(true, ex);
        } else {
            tuiUpdater.setCandidateNick("");
            tuiUpdater.setTuiState(TuiStates.CONNECTING);
            tuiUpdater.setHomeState(TuiStates.CONNECTING);
            tuiUpdater.getCurrentTuiState().restart(true, ex);
        }
    }

    /**
     * Handles the NotGodPlayerException, thrown when the player tries to perform an action that is
     * allowed only to the god player
     *
     * @param ex the exception thrown
     */
    @Override
    public void throwException(@NotNull NotGodPlayerException ex) {
        LOGGER.debug("NotGodPlayerException {}", ex.getMessage());
        model.setGodPlayer(null);
        tuiUpdater.setTuiState(TuiStates.SETTING_NAME);
        tuiUpdater.setHomeState(TuiStates.SETTING_NAME);
        tuiUpdater.getCurrentTuiState().restart(true, ex);
    }

    /**
     * Handles the GameStatusException, thrown when the player tries to perform an action that is
     * not allowed in the current game status
     *
     * @param ex the exception thrown
     */
    @Override
    public void throwException(@NotNull GameStatusException ex) {
        LOGGER.debug("GameStatusException received: {}", ex.getMessage());

    }

    /**
     * Handles the NotSetNumOfPlayerException, thrown when the player tries to connect before the
     * god player has set the number of players
     *
     * @param ex the exception thrown
     */
    @Override
    public void throwException(@NotNull NotSetNumOfPlayerException ex) {
        LOGGER.debug("NotSetNumOfPlayerException received: {}", ex.getMessage());
        if (tuiUpdater.isCurrentState(TuiStates.WAITING)) {
            tuiUpdater.setCandidateNick("");
            tuiUpdater.setTuiState(TuiStates.SETTING_NAME);
            tuiUpdater.setHomeState(TuiStates.SETTING_NAME);
            tuiUpdater.getCurrentTuiState().restart(true, ex);
        }
    }

    /**
     * Handles the IllegalPlateauActionException, thrown when the player tries to perform an action
     * that is not allowed on the plateau
     *
     * @param ex the exception thrown
     */
    @Override
    public void throwException(@NotNull IllegalPlateauActionException ex) {
        LOGGER.debug("IllegalPlateauActionException {}", ex.getMessage());
        tuiUpdater.getCurrentTuiState().restart(true, ex);
    }

    /**
     * Handles the MaxHandSizeException, thrown when the player tries to draw a card when his hand
     * is full
     *
     * @param ex the exception thrown
     */
    @Override
    public void throwException(@NotNull MaxHandSizeException ex) {
        LOGGER.debug("MaxHandSizeException {}", ex.getMessage());
        tuiUpdater.getCurrentTuiState().restart(true, ex);
    }

}
