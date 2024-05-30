package it.polimi.ingsw.am11.chat;

import it.polimi.ingsw.am11.network.connector.ClientChatConnector;

public class ClientChatController {

    private final String nickname;
    private final ClientChatConnector clientChatConnector;

    public ClientChatController(String nickname,
                                ClientChatConnector clientChatConnector) {
        this.nickname = nickname;
        this.clientChatConnector = clientChatConnector;
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
        clientChatConnector.pubPrivateMsg(recipient, msg);
    }

    private void pubMsg(String msg) {
        clientChatConnector.pubMsg(msg);
    }

    public void receiveMsg(String msg) {
        //TODO
    }


}
