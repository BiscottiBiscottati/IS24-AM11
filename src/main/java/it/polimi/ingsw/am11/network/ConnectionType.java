package it.polimi.ingsw.am11.network;

import it.polimi.ingsw.am11.network.Socket.Client.ClientSocket;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;

import java.util.function.BiFunction;

public enum ConnectionType {
    //FIXME ClientMain does not implement ClientNetWorkHandler
    //RMI((port, updater) -> new ClientMain("localhost", port)),
    SOCKET((port, updater) -> new ClientSocket("localhost", port, updater));

    final BiFunction<Integer, ClientViewUpdater, ClientNetworkHandler> creator;

    ConnectionType(BiFunction<Integer, ClientViewUpdater, ClientNetworkHandler> creator) {
        this.creator = creator;
    }

//    public ClientNetworkHandler getHandler(String ip, int port, ClientViewUpdater updater) {
//        return creator
//    }
}
