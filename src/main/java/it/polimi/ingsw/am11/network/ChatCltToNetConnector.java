package it.polimi.ingsw.am11.network;

public interface ChatCltToNetConnector {
    void pubMsg(String sender, String msg);

    void pubPrivMsg(String sender, String recipient, String msg);
}
