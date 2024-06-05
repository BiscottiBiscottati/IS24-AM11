package it.polimi.ingsw.am11.model.utils;

import it.polimi.ingsw.am11.model.GameLogic;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.model.utils.resilience.ReconnectionTimer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReconnectionTimerTest {
    static int halfReconnection;
    @Mock
    GameLogic model;

    ReconnectionTimer timer;
    ReconnectionTimer timer2;

    @BeforeAll
    static void beforeAll() {
        ReconnectionTimer.setReconnectionTime(100);
        halfReconnection = ReconnectionTimer.getReconnectionTime() >> 1;
    }

    @BeforeEach
    void setUp()
    throws GameBreakingException, GameStatusException, IllegalPlayerSpaceActionException,
           TurnsOrderException, IllegalPickActionException, PlayerInitException {
        timer = new ReconnectionTimer(model);
        timer2 = new ReconnectionTimer(model);

        doNothing().when(model).goNextTurn();
        doNothing().when(model).endGameEarly();
        when(model.drawVisibleOf(any(), any(), anyInt())).thenReturn(1);
        when(model.getExposedCards(any())).thenReturn(Set.of(1, 2));
    }

    @AfterEach
    void tearDown() {
        reset(model);
    }

    @Test
    void disconnectCurrent() throws InterruptedException, GameBreakingException,
                                    GameStatusException, IllegalPlayerSpaceActionException,
                                    TurnsOrderException, IllegalPickActionException,
                                    PlayerInitException {

        timer.disconnectCurrent("nickname", TurnAction.PLACE_CARD);

        Thread.sleep(ReconnectionTimer.getReconnectionTime() + 100);
        verify(model, times(1)).goNextTurn();

        timer2.disconnectCurrent("nickname", TurnAction.DRAW_CARD);

        Thread.sleep(ReconnectionTimer.getReconnectionTime() + 100);
        verify(model, times(1)).drawVisibleOf(any(), any(), anyInt());
        verify(model, times(1)).getExposedCards(any());
    }

    @Test
    void reconnectCurrent() throws InterruptedException, GameBreakingException,
                                   GameStatusException, IllegalPlayerSpaceActionException,
                                   TurnsOrderException, IllegalPickActionException,
                                   PlayerInitException {
        timer.disconnectCurrent("nickname", TurnAction.PLACE_CARD);
        Thread.sleep(halfReconnection);
        timer.reconnectCurrent();
        Thread.sleep(halfReconnection + 100);
        verify(model, never()).goNextTurn();

        timer2.disconnectCurrent("nickname", TurnAction.DRAW_CARD);
        Thread.sleep(halfReconnection);
        timer2.reconnectCurrent();
        Thread.sleep(halfReconnection + 100);
        verify(model, never()).drawVisibleOf(any(), any(), anyInt());
        verify(model, never()).getExposedCards(any());
    }

    @Test
    void waitForReconnection() throws InterruptedException {
        timer.waitForReconnection();

        Thread.sleep(ReconnectionTimer.getReconnectionTime() + 100);
        verify(model, times(1)).endGameEarly();
    }

    @Test
    void reconnect() throws InterruptedException {
        timer.waitForReconnection();
        Thread.sleep(halfReconnection);
        timer.reconnect();

        Thread.sleep(halfReconnection + 100);
        verify(model, never()).endGameEarly();
    }
}