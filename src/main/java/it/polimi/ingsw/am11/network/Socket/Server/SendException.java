package it.polimi.ingsw.am11.network.Socket.Server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.PrintWriter;

public class SendException {
    private final PrintWriter out;
    private final ObjectMapper mapper = new ObjectMapper();

    public SendException(PrintWriter out) {
        this.out = out;
    }

    public void IllegalPlayerSpaceActionException() {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "IllegalPlayerSpaceActionException");
        out.println(json);
    }

    public void TurnsOrderException() {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "TurnsOrderException");
        out.println(json);
    }

    public void PlayerInitException() {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "PlayerInitException");
        out.println(json);
    }

    public void IllegalCardPlacingException() {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "IllegalCardPlacingException");
        out.println(json);
    }

    public void IllegalPickActionException() {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "IllegalPickActionException");
        out.println(json);
    }

    public void NotInHandException() {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "NotInHandException");
        out.println(json);
    }

    public void EmptyDeckException() {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "EmptyDeckException");
        out.println(json);
    }

    public void IllegalPlateauActionException() {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "IllegalPlateauActionException");
        out.println(json);
    }

    public void MaxHandSizeException() {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "MaxHandSizeException");
        out.println(json);
    }

    public void GameStatusException() {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "GameStatusException");
        out.println(json);
    }
}
