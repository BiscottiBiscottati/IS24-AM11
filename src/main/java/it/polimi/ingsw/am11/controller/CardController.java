package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.model.GameModel;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.model.utils.persistence.SavesManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CardController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CardController.class);
    private final @NotNull GameModel model;
    private final @NotNull ExecutorService saveExecutor;

    CardController(@NotNull GameModel model) {
        this.model = model;
        this.saveExecutor = Executors.newSingleThreadExecutor();
    }

    public synchronized void setObjectiveFor(@NotNull String nickname, int cardID)
    throws GameStatusException,
           PlayerInitException,
           IllegalPlayerSpaceActionException {
        try {
            model.setObjectiveFor(nickname, cardID);
            saveExecutor.submit(this::saveToDisk);
        } catch (GameBreakingException e) {
            throw new RuntimeException(e);
        }
    }

    synchronized void saveToDisk() {
        LOGGER.info("CONTROLLER: Saving game to disk");
        SavesManager.saveGame(model.save());
    }

    public synchronized void placeCard(@NotNull String Nickname,
                                       int cardId,
                                       @NotNull Position position,
                                       boolean isRetro)
    throws GameStatusException,
           PlayerInitException,
           IllegalCardPlacingException,
           TurnsOrderException,
           IllegalPlateauActionException,
           NotInHandException {
        model.placeCard(Nickname, cardId, position, isRetro);
        saveExecutor.submit(this::saveToDisk);
    }

    public synchronized void drawCard(boolean fromVisible, @NotNull PlayableCardType type,
                                      @NotNull String nickname,
                                      int cardID)
    throws IllegalPlayerSpaceActionException, TurnsOrderException, IllegalPickActionException,
           PlayerInitException, GameStatusException, EmptyDeckException,
           MaxHandSizeException {
        try {
            if (fromVisible) {
                model.drawVisibleOf(type, nickname, cardID);
            } else model.drawFromDeckOf(type, nickname);
            saveExecutor.submit(this::saveToDisk);
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
            saveExecutor.submit(this::saveToDisk);
        } catch (GameBreakingException e) {
            throw new RuntimeException(e);
        }
    }
}
