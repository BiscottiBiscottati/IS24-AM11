package it.polimi.ingsw.am11.network.Socket.Client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am11.model.exceptions.*;

public class ReceiveException {
    private final ObjectMapper mapper;
    private Exception exception;

    public ReceiveException() {
        this.mapper = new ObjectMapper();
    }

    public void receive(String message) {
        try {
            // Parse the message
            JsonNode jsonNode = mapper.readTree(message);
            // Switch on the method
            if (jsonNode.get("method").asText().equals("Exception")) {
                switch (jsonNode.get("message").asText()) {
                    case "IllegalPlayerSpaceActionException":
                        exception = new IllegalPlayerSpaceActionException(
                                jsonNode.get("description")
                                        .asText());
                    case "TurnsOrderException":
                        exception = new TurnsOrderException(jsonNode.get("description").asText());
                    case "PlayerInitException":
                        exception = new PlayerInitException(jsonNode.get("description").asText());
                    case "IllegalCardPlacingException":
                        exception = new IllegalCardPlacingException(jsonNode.get("description")
                                                                            .asText());
                    case "IllegalPickActionException":
                        exception = new IllegalPickActionException(jsonNode.get("description")
                                                                           .asText());
                    case "NotInHandException":
                        exception = new NotInHandException(jsonNode.get("description").asText());
                    case "EmptyDeckException":
                        exception = new EmptyDeckException(jsonNode.get("description").asText());
                    case "NumOfPlayersException":
                        exception = new NumOfPlayersException(jsonNode.get("description").asText());
                    case "NotGodPlayerException":
                        exception = new NotGodPlayerException(jsonNode.get("description").asText());
                    case "GameStatusException":
                        exception = new GameStatusException(jsonNode.get("description").asText());
                    case "NotSetNumOfPlayerException":
                        exception = new NotSetNumOfPlayerException(jsonNode.get("description")
                                                                           .asText());
                }
                //TODO: Throw the exception
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
