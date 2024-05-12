package it.polimi.ingsw.am11;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.network.Socket.Server.SocketManager;

public class Server {
    static final int DEFAULT_SOCKET_PORT = 12345;
    static final int DEFAULT_RMI_PORT = 12346;
    static final CentralController centralController = CentralController.INSTANCE;

    static void start(String[] args) {
        new SocketManager(DEFAULT_SOCKET_PORT).start();
        try {
            centralController.connectPlayer("test", null, null);
            System.out.println(centralController.getGodPlayer());
        } catch (PlayerInitException e) {
            throw new RuntimeException(e);
        } catch (GameStatusException e) {
            throw new RuntimeException(e);
        } catch (NumOfPlayersException e) {
            throw new RuntimeException(e);
        }
    }
}
