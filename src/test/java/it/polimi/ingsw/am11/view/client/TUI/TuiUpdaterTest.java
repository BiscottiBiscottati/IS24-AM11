package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.network.connector.ServerChatConnector;
import it.polimi.ingsw.am11.network.connector.ServerPlayerConnector;
import it.polimi.ingsw.am11.network.connector.ServerTableConnector;

import it.polimi.ingsw.am11.view.client.TUI.states.TuiStates;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TuiUpdaterTest {

    @Mock
    ServerPlayerConnector serverPlayerConnectorPippo;
    ServerPlayerConnector serverPlayerConnectorPluto;

    ServerTableConnector serverTableConnectorPippo;
    ServerTableConnector serverTableConnectorPluto;

    ServerChatConnector serverChatConnectorPippo;
    ServerChatConnector serverChatConnectorPluto;

    @Test
    void reset() {

        TuiUpdater tuiUpdater = new TuiUpdater(TuiStates.CONNECTING);
        tuiUpdater.reset(TuiStates.CONNECTING);

        assertEquals(tuiUpdater.getState(TuiStates.CONNECTING), tuiUpdater.getCurrentTuiState());
        tuiUpdater.goBack();
        assertEquals(tuiUpdater.getState(TuiStates.CONNECTING), tuiUpdater.getCurrentTuiState());

    }

    @Test
    void updateDeckTop() {


    }

    @Test
    void updateField() {
    }

    @Test
    void updateShownPlayable() {
    }

    @Test
    void updateTurnChange() {
    }

    @Test
    void updatePlayerPoint() {
    }

    @Test
    void updateGameStatus() {
    }

    @Test
    void updateCommonObjective() {
    }

    @Test
    void receiveFinalLeaderboard() {
    }

    @Test
    void updateHand() {
    }

    @Test
    void updatePersonalObjective() {
    }

    @Test
    void receiveStarterCard() {
    }

    @Test
    void receiveCandidateObjective() {
    }

    @Test
    void notifyGodPlayer() {
    }

    @Test
    void updatePlayers() {
    }

    @Test
    void updateNumOfPlayers() {
    }

    @Test
    void disconnectedFromServer() {
    }

    @Test
    void receiveReconnection() {

//        TuiUpdater pippoTuiUpdater = new TuiUpdater(TuiStates.CONNECTING);
//        TuiUpdater plutoTuiUpdater = new TuiUpdater(TuiStates.CONNECTING);
//
//        VirtualPlayerView pippoView;
//        VirtualPlayerView plutoView;
//
//        CentralController.INSTANCE.createNewGame();
//
//        try {
//            pippoView = CentralController.INSTANCE.connectPlayer("Pippo",
//                                                                 serverPlayerConnectorPippo,
//                                                                 serverTableConnectorPippo,
//                                                                 serverChatConnectorPippo);
//            pippoTuiUpdater.setCandidateNick("Pippo");
//            pippoTuiUpdater.notifyGodPlayer();
//
//            pippoView.setNumOfPlayers(2);
//
//
//            plutoView = CentralController.INSTANCE.connectPlayer("Pluto",
//                                                                 serverPlayerConnectorPluto,
//                                                                 serverTableConnectorPluto,
//                                                                 serverChatConnectorPluto);
//            plutoTuiUpdater.setCandidateNick("Pluto");
//        } catch (GameStatusException | NumOfPlayersException | PlayerInitException |
//                 NotSetNumOfPlayerException | NotGodPlayerException e) {
//            throw new RuntimeException(e);
//        }


    }

    @Test
    void getExceptionThrower() {
    }

    @Test
    void getChatUpdater() {
    }

    @Test
    void setTuiState() {
    }

    @Test
    void isCurrentState() {
    }

    @Test
    void receiveMsg() {
    }

    @Test
    void receivePrivateMsg() {
    }

    @Test
    void confirmSentMsg() {
    }

    @Test
    void getCandidateNick() {
    }

    @Test
    void setCandidateNick() {
    }

    @Test
    void getCurrentTuiState() {
    }

    @Test
    void getState() {
    }

    @Test
    void goBack() {
    }

    @Test
    void setHomeState() {
    }


}