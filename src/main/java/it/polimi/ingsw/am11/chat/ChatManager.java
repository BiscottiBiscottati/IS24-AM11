package it.polimi.ingsw.am11.chat;

import it.polimi.ingsw.am11.network.ChatSrvToNetConnector;

import java.util.Set;

public class ChatManager {

    private final Set<String> playerList;
    private final ChatSrvToNetConnector chatConnector;

    public ChatManager(Set<String> playerList, ChatSrvToNetConnector chatConnector) {
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
        for (String player : playerList) {
            chatConnector.sendMsg(player, sender + ": " + msg);
        }
    }

    public void pubPrivMsg(String sender, String recipient, String msg) {
        if (playerList.contains(recipient)) {
            chatConnector.sendMsg(recipient, "PRV[" + sender + "]: " + msg);
            chatConnector.sendMsg(sender, "TO [" + recipient + "]: " + msg);
        } else {
            chatConnector.sendMsg(sender, "player " + recipient + " not found");
        }
    }


}
