package it.polimi.ingsw.am11.network.socket;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

/**
 * The socket interface for receive messages
 */
public interface MessageReceiver {
    void receive(@NotNull JsonNode jsonNode);
}
