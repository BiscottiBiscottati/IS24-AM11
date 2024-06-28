package it.polimi.ingsw.am11.network.socket;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

/**
 * The socket interface for receive messages
 */
public interface MessageReceiver {

    /**
     * This method is called to send a json message both from the server and the client
     *
     * @param jsonNode the json message
     */
    void receive(@NotNull JsonNode jsonNode);
}
