package it.polimi.ingsw.am11.model.utils.resilience;

import it.polimi.ingsw.am11.model.GameModel;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.model.utils.TurnAction;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ReconnectionTimer {
    private static int RECONNECTION_TIME = 10000;

    private final @NotNull Timer currentTurnTimer;
    private final @NotNull Timer reconnectionTimer;
    private final @NotNull GameModel model;

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
                        model.goNextTurn();
                    } catch (GameBreakingException | GameStatusException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, RECONNECTION_TIME);
            case DRAW_CARD -> currentTurnTimer.schedule(new TimerTask() {
                @Override
                public void run() {
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
        currentTurnTimer.cancel();
    }

    public void waitForReconnection() {
        reconnectionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                model.endGameEarly();
            }
        }, RECONNECTION_TIME);
    }

    public void reconnect() {
        reconnectionTimer.cancel();
    }
}
