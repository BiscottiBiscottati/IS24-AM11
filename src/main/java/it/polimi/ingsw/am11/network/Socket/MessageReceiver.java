package it.polimi.ingsw.am11.network.Socket;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

public interface MessageReceiver {
    void receive(@NotNull JsonNode jsonNode);
}
