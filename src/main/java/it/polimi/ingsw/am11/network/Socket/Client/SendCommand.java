package it.polimi.ingsw.am11.network.Socket.Client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.PrintWriter;

public class SendCommand {
    // This class is used to send commands to the server
    // It is used by the client to send commands to the server
    private final PrintWriter out;
    private final ObjectMapper mapper = new ObjectMapper();

    public SendCommand(PrintWriter out) {
        this.out = out;
    }

    public void setStarterCard(boolean isRetro) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "setStarterCard");
        json.put("isRetro", isRetro);
        out.println(json);
    }

}
