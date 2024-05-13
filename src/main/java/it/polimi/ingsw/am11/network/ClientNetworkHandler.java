package it.polimi.ingsw.am11.network;

import java.io.IOException;

public interface ClientNetworkHandler {

    void connect(String nickname) throws IOException;

    void setNumOfPlayers(int numOfPlayers) throws IOException;
}
