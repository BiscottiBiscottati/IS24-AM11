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
import java.util.Timer;
import java.util.TimerTask;

public class ReconnectionTimer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReconnectionTimer.class);
    private static final int TOTAL_RECONNECTION_TIME = 30000;
    private static int RECONNECTION_TIME = 10000;
    private final @NotNull Timer currentTurnTimer;
    private final @NotNull Timer reconnectionTimer;
    private final @NotNull GameModel model;
    private boolean isWaitingForReconnection = false;
    private int numberOfDisconnected = 0;

    public ReconnectionTimer(@NotNull GameModel model) {
        this.model = model;
        this.currentTurnTimer = new Timer();
        this.reconnectionTimer = new Timer();
    }

    public static int getReconnectionTime() {
        return RECONNECTION_TIME;
    }

    public static void setReconnectionTime(int reconnectionTime) {
        RECONNECTION_TIME = reconnectionTime;
    }

    public void disconnectCurrent(@NotNull String nickname,
                                  @NotNull TurnAction turnAction) {
        switch (turnAction) {
            case PLACE_CARD -> currentTurnTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        LOGGER.info("MODEL: disconnected current player during placing, " +
                                    "skipping its turn");
                        model.goNextTurn();
                    } catch (GameBreakingException | GameStatusException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, RECONNECTION_TIME);
            case DRAW_CARD -> currentTurnTimer.schedule(new TimerTask() {
                @Override
                public void run() {
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
                }
            }, RECONNECTION_TIME);
        }
    }

    public void reconnectCurrent() {
        LOGGER.info("MODEL: reconnected current player, cancelling timer");
        currentTurnTimer.cancel();
    }

    public void waitForReconnection() {
        this.numberOfDisconnected = 1;
        isWaitingForReconnection = true;
        reconnectionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                LOGGER.info("MODEL: started timer for reconnection");
                isWaitingForReconnection = false;
                model.endGameEarly();
            }
        }, RECONNECTION_TIME);
    }

    public void reconnect() {
        if (numberOfDisconnected == 1) {
            LOGGER.info("MODEL: all players reconnected, stopping timer");
            reconnectionTimer.cancel();
            isWaitingForReconnection = false;
        }
        numberOfDisconnected--;
    }

    public void waitForTotalReconnection() {
        if (isWaitingForReconnection) {
            numberOfDisconnected++;
            return;
        }
        numberOfDisconnected = 1;
        isWaitingForReconnection = true;
        reconnectionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                LOGGER.info("MODEL: started timer for total reconnection");
                isWaitingForReconnection = false;
                CentralController.INSTANCE.destroyGame();
                currentTurnTimer.cancel();
            }
        }, TOTAL_RECONNECTION_TIME);
    }

    public void cancelAll() {
        LOGGER.info("MODEL: cancelling all timers");
        reconnectionTimer.cancel();
        currentTurnTimer.cancel();
    }
}
