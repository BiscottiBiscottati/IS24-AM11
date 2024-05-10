package it.polimi.ingsw.am11.chat;

import it.polimi.ingsw.am11.network.ChatCltToNetConnector;

public class ClientChatController {

    private final String nickname;
    private final ChatCltToNetConnector chatCltToNetConnector;

    public ClientChatController(String nickname,
                                ChatCltToNetConnector chatCltToNetConnector) {
        this.nickname = nickname;
        this.chatCltToNetConnector = chatCltToNetConnector;
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
        chatCltToNetConnector.pubPrivateMsg(nickname, recipient, msg);
    }

    private void pubMsg(String msg) {
        chatCltToNetConnector.pubMsg(nickname, msg);
    }

    public void receiveMsg(String msg) {
        //TODO
    }


}
