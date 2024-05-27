package it.polimi.ingsw.am11.network.Socket.Server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;

public class ServerExceptionSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerExceptionSender.class);

    private final @NotNull PrintWriter out;
    private final @NotNull ObjectMapper mapper;

    public ServerExceptionSender(@NotNull PrintWriter out) {
        this.out = out;
        this.mapper = new ObjectMapper();
    }

    public void exception(@NotNull Exception e) {
        LOGGER.info("SERVER TCP: Exception to send: {}", e.getMessage());
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

    private void IllegalPlayerSpaceActionException(@NotNull Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "IllegalPlayerSpaceActionException");
        json.put("description", e.getMessage());
        out.println(json);
    }

    private void TurnsOrderException(@NotNull Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "TurnsOrderException");
        json.put("description", e.getMessage());
        out.println(json);
    }

    private void PlayerInitException(@NotNull Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "PlayerInitException");
        json.put("description", e.getMessage());
        out.println(json);
    }

    private void IllegalCardPlacingException(@NotNull Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "IllegalCardPlacingException");
        json.put("description", e.getMessage());
        out.println(json);
    }

    private void IllegalPickActionException(@NotNull Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "IllegalPickActionException");
        json.put("description", e.getMessage());
        out.println(json);
    }

    private void NotInHandException(@NotNull Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "NotInHandException");
        json.put("description", e.getMessage());
        out.println(json);
    }

    private void EmptyDeckException(@NotNull Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "EmptyDeckException");
        json.put("description", e.getMessage());
        out.println(json);
    }

    private void IllegalPlateauActionException(@NotNull Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "IllegalPlateauActionException");
        json.put("description", e.getMessage());
        out.println(json);
    }

    private void MaxHandSizeException(@NotNull Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "MaxHandSizeException");
        json.put("description", e.getMessage());
        out.println(json);
    }

    private void GameStatusException(@NotNull Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "GameStatusException");
        json.put("description", e.getMessage());
        out.println(json);
    }

    private void NumOfPlayersException(@NotNull Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "NumOfPlayersException");
        json.put("description", e.getMessage());
        out.println(json);
    }

    private void NotSetNumOfPlayerException(@NotNull Exception e) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "Exception");
        json.put("message", "NotSetNumOfPlayerException");
        json.put("description", e.getMessage());
        out.println(json);
    }
}
