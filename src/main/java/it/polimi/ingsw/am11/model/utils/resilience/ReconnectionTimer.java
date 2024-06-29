package it.polimi.ingsw.am11.model.utils.resilience;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.model.GameModel;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.model.utils.TurnAction;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ReconnectionTimer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReconnectionTimer.class);
    private static final int TOTAL_RECONNECTION_TIME = 30000;
    private static int RECONNECTION_TIME = 10000;
    private final @NotNull ScheduledExecutorService executor;
    private final @NotNull GameModel model;
    private ScheduledFuture<?> currentTurnFuture;
    private ScheduledFuture<?> reconnectionFuture;
    private ScheduledFuture<?> totalReconnectionFuture;
    private boolean isWaitingForReconnection = false;
    private int numberOfDisconnected = 0;

    public ReconnectionTimer(@NotNull GameModel model) {
        this.model = model;
        this.executor = Executors.newScheduledThreadPool(3);
        this.currentTurnFuture = null;
        this.reconnectionFuture = null;
        this.totalReconnectionFuture = null;
    }

    public static int getReconnectionTime() {
        return RECONNECTION_TIME;
    }

    public static void setReconnectionTime(int reconnectionTime) {
        RECONNECTION_TIME = reconnectionTime;
    }

    public void disconnectCurrent(@NotNull String nickname,
                                  @NotNull TurnAction turnAction) {
        LOGGER.info("MODEL: disconnected current player, starting current timer");

        Runnable runnable = getRunnable(nickname, turnAction);
        currentTurnFuture = executor.schedule(runnable, RECONNECTION_TIME, TimeUnit.MILLISECONDS);
    }

    private @NotNull Runnable getRunnable(String nickname, @NotNull TurnAction turnAction) {
        Runnable runnable = () -> {};

        switch (turnAction) {
            case PLACE_CARD -> runnable = () -> {
                try {
                    LOGGER.info("MODEL: disconnected current player during placing, " +
                                "skipping its turn");
                    model.goNextTurn();
                } catch (GameBreakingException | GameStatusException e) {
                    throw new RuntimeException(e);
                }
            };
            case DRAW_CARD -> runnable = () -> {
                LOGGER.info("MODEL: disconnected current player during drawing, " +
                            "drawing a random card");
                Random generator = new Random();
                PlayableCardType type = PlayableCardType.values()[generator.nextInt(2)];
                try {
                    model.getExposedCards(type).stream()
                         .findFirst()
                         .ifPresent(card -> {
                             try {
                                 model.drawVisibleOf(type, nickname, card);
                             } catch (GameBreakingException | GameStatusException |
                                      TurnsOrderException | IllegalPlayerSpaceActionException |
                                      IllegalPickActionException | PlayerInitException e) {
                                 throw new RuntimeException(e);
                             }
                         });
                } catch (GameStatusException e) {
                    throw new RuntimeException(e);
                }
            };
        }
        return runnable;
    }

    public void reconnectCurrent() {
        LOGGER.info("MODEL: reconnected current player, cancelling timer");
        if (currentTurnFuture != null) currentTurnFuture.cancel(false);
    }

    public void waitForReconnection() {
        LOGGER.info("MODEL: started timer for reconnection");
        reconnectionFuture = executor.schedule(() -> {
            LOGGER.info("MODEL: no player reconnected in time, ending game");
            model.endGameEarly();
        }, RECONNECTION_TIME, TimeUnit.MILLISECONDS);
    }

    public void reconnect() {
        if (reconnectionFuture != null) reconnectionFuture.cancel(false);
        if (numberOfDisconnected == 1) {
            LOGGER.info("MODEL: enough players reconnected, stopping total reconnection timer");
            if (totalReconnectionFuture != null) totalReconnectionFuture.cancel(false);
            isWaitingForReconnection = false;
        }
        if (numberOfDisconnected > 0) numberOfDisconnected--;
    }

    public void waitForTotalReconnection() {
        if (isWaitingForReconnection) {
            numberOfDisconnected++;
            return;
        }
        numberOfDisconnected = 1;
        isWaitingForReconnection = true;
        LOGGER.info("MODEL: started timer for total reconnection");
        totalReconnectionFuture = executor.schedule(() -> {
            LOGGER.info("MODEL: total reconnection failed, destroying the game");
            isWaitingForReconnection = false;
            CentralController.INSTANCE.destroyGame();
            executor.shutdown();
        }, TOTAL_RECONNECTION_TIME, TimeUnit.MILLISECONDS);
    }

    public void cancelAll() {
        LOGGER.info("MODEL: cancelling all timers");
        if (currentTurnFuture != null) currentTurnFuture.cancel(false);
        if (reconnectionFuture != null) reconnectionFuture.cancel(false);
        if (totalReconnectionFuture != null) totalReconnectionFuture.cancel(false);
    }

    public boolean isWaitingForReconnection() {
        return isWaitingForReconnection;
    }
}
