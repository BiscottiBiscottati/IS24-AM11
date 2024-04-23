package it.polimi.ingsw.am11.network;

class SocketManagerTest {
    @org.junit.jupiter.api.Test
    void start() {
        SocketManager socketManager = new SocketManager(1234);

    }

    @org.junit.jupiter.api.Test
    void stop() {
        SocketManager socketManager = new SocketManager(1234);
        socketManager.stop();
    }
}