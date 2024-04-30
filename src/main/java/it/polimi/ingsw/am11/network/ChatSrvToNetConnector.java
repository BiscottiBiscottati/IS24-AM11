package it.polimi.ingsw.am11.network;

public interface ChatSrvToNetConnector {

    void sendMsg(String recipient, String msg);
}
