package it.polimi.ingsw.am11.network;

public interface ClientNetworkHandler {

    void connect(String nickname);

    void setNumOfPlayers(int numOfPlayers);
}
