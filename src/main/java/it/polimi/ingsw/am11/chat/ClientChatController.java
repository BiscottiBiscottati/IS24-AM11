package it.polimi.ingsw.am11.chat;

import it.polimi.ingsw.am11.view.client.ChatClientConnector;

public class ClientChatController {

    private final String nickname;
    private final ChatClientConnector chatClientConnector;

    public ClientChatController(String nickname,
                                ChatClientConnector chatClientConnector) {
        this.nickname = nickname;
        this.chatClientConnector = chatClientConnector;
    }


    public void writeMessage(String msg) {
        msg = msg.strip();
        int i = msg.indexOf(' ');
        if (msg.substring(0, i).equalsIgnoreCase("/msg")) {
            String rest = msg.substring(i + 1);
            rest = rest.strip();
            i = rest.indexOf(' ');
            if (i > 0) {
                pubPrivMsg(rest.substring(0, i), rest.substring(i + 1).strip());
            }
        } else {
            pubMsg(msg);
        }
    }

    private void pubPrivMsg(String recipient, String msg) {
        chatClientConnector.pubPrivMsg(nickname, recipient, msg);
    }

    private void pubMsg(String msg) {
        chatClientConnector.pubMsg(nickname, msg);
    }

    public void receiveMsg(String msg) {
        //TODO
    }


}
