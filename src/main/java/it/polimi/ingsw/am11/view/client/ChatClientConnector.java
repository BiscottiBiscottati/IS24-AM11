package it.polimi.ingsw.am11.view.client;

public interface ChatClientConnector {
    void pubMsg(String sender, String msg);

    void pubPrivMsg(String sender, String recipient, String msg);
}
