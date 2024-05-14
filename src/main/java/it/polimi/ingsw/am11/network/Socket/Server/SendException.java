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

    public void Exception(Exception e) {
        switch (e.getClass().getSimpleName()) {
            case "IllegalPlayerSpaceActionException":
                IllegalPlayerSpaceActionException(e);
                break;
            case "TurnsOrderException":
                TurnsOrderException(e);
                break;
            case "PlayerInitException":
                PlayerInitException(e);
                break;
            case "IllegalCardPlacingException":
                IllegalCardPlacingException(e);
                break;
            case "IllegalPickActionException":
                IllegalPickActionException(e);
                break;
            case "NotInHandException":
                NotInHandException(e);
                break;
            case "EmptyDeckException":
                EmptyDeckException(e);
                break;
            case "IllegalPlateauActionException":
                IllegalPlateauActionException(e);
                break;
            case "MaxHandSizeException":
                MaxHandSizeException(e);
                break;
            case "GameStatusException":
                GameStatusException(e);
                break;
            case "NumOfPlayersException":
                NumOfPlayersException(e);
            case "NotSetNumOfPlayerException":
                NotSetNumOfPlayerException(e);
        }
    }

    private void IllegalPlayerSpaceActionException(Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "IllegalPlayerSpaceActionException");
        json.put("description", e.getMessage());
        out.println(json);
    }

    private void TurnsOrderException(Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "TurnsOrderException");
        json.put("description", e.getMessage());
        out.println(json);
    }

    private void PlayerInitException(Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "PlayerInitException");
        json.put("description", e.getMessage());
        out.println(json);
    }

    private void IllegalCardPlacingException(Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "IllegalCardPlacingException");
        json.put("description", e.getMessage());
        out.println(json);
    }

    private void IllegalPickActionException(Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "IllegalPickActionException");
        json.put("description", e.getMessage());
        out.println(json);
    }

    private void NotInHandException(Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "NotInHandException");
        json.put("description", e.getMessage());
        out.println(json);
    }

    private void EmptyDeckException(Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "EmptyDeckException");
        json.put("description", e.getMessage());
        out.println(json);
    }

    private void IllegalPlateauActionException(Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "IllegalPlateauActionException");
        json.put("description", e.getMessage());
        out.println(json);
    }

    private void MaxHandSizeException(Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "MaxHandSizeException");
        json.put("description", e.getMessage());
        out.println(json);
    }

    private void GameStatusException(Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "GameStatusException");
        json.put("description", e.getMessage());
        out.println(json);
    }

    private void NumOfPlayersException(Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "NumOfPlayersException");
        json.put("description", e.getMessage());
        out.println(json);
    }

    private void NotSetNumOfPlayerException(Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "NotSetNumOfPlayerException");
        json.put("description", e.getMessage());
        out.println(json);
    }
}
