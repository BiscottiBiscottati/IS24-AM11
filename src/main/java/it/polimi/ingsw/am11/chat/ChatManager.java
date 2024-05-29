package it.polimi.ingsw.am11.chat;

import it.polimi.ingsw.am11.network.ServerChatConnector;

import java.util.Set;

public class ChatManager {

    private final Set<String> playerList;
    private final ServerChatConnector chatConnector;

    public ChatManager(Set<String> playerList, ServerChatConnector chatConnector) {
        this.playerList = playerList;
        this.chatConnector = chatConnector;
    }

    public void addPlayer(String nickname) {
        playerList.add(nickname);
    }

    public void rmPlayer(String nickname) {
        playerList.remove(nickname);
    }

    public void pubMessage(String sender, String msg) {
    }


}
