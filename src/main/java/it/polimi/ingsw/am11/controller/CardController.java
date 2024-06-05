package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.model.GameModel;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.persistence.SavesManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CardController {
    private static final long SAVE_INTERVAL_SEC = 5;

    private static final Logger LOGGER = LoggerFactory.getLogger(CardController.class);

    private final @NotNull GameModel model;
    private final @NotNull ScheduledExecutorService saveExecutor;

    CardController(@NotNull GameModel model) {
        this.model = model;
        this.saveExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    public synchronized void setObjectiveFor(@NotNull String nickname, int cardID)
    throws GameStatusException,
           PlayerInitException,
           IllegalPlayerSpaceActionException {
        try {
            model.setObjectiveFor(nickname, cardID);
        } catch (GameBreakingException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void placeCard(@NotNull String Nickname, int cardId,
                                       @NotNull Position position,
                                       boolean isRetro)
    throws GameStatusException,
           PlayerInitException,
           IllegalCardPlacingException,
           TurnsOrderException,
           IllegalPlateauActionException,
           NotInHandException {
        model.placeCard(Nickname, cardId, position, isRetro);
    }

    public synchronized int drawCard(boolean fromVisible, @NotNull PlayableCardType type,
                                     @NotNull String nickname,
                                     int cardID)
    throws IllegalPlayerSpaceActionException, TurnsOrderException, IllegalPickActionException,
           PlayerInitException, GameStatusException, EmptyDeckException,
           MaxHandSizeException {
        try {
            if (fromVisible) {
                return model.drawVisibleOf(type, nickname, cardID);
            } else return model.drawFromDeckOf(type, nickname);
        } catch (GameBreakingException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void setStarterFor(@NotNull String nickname, boolean isRetro)
    throws GameStatusException,
           PlayerInitException,
           IllegalCardPlacingException {
        try {
            model.setStarterFor(nickname, isRetro);
        } catch (GameBreakingException e) {
            throw new RuntimeException(e);
        }
    }

    void startSaveExecutor() {
        saveExecutor.scheduleAtFixedRate(this::saveToDisk,
                                         SAVE_INTERVAL_SEC,
                                         SAVE_INTERVAL_SEC,
                                         TimeUnit.SECONDS);
    }

    synchronized void saveToDisk() {
        LOGGER.info("CONTROLLER: Saving game to disk");
        SavesManager.saveGame(model.save());
    }

    void stopSaveExecutor() {
        saveExecutor.shutdown();
    }
}
