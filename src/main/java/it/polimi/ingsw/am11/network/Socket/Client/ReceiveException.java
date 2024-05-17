package it.polimi.ingsw.am11.network.Socket.Client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.view.client.ExceptionConnector;

public class ReceiveException {
    private final ObjectMapper mapper;
    private final ExceptionConnector exceptionConnector;

    public ReceiveException(ExceptionConnector exceptionConnector) {
        this.mapper = new ObjectMapper();
        this.exceptionConnector = exceptionConnector;
    }

    public void receive(String message) {
        try {
            // Parse the message
            JsonNode jsonNode = mapper.readTree(message);
            // Switch on the method
            switch (jsonNode.get("message").asText()) {
                case "IllegalPlayerSpaceActionException":
                    exceptionConnector.throwException(new IllegalPlayerSpaceActionException(
                            jsonNode.get("description")
                                    .asText()));
                case "TurnsOrderException":
                    exceptionConnector.throwException(
                            new TurnsOrderException(jsonNode.get("description").asText()));

                case "PlayerInitException":
                    exceptionConnector.throwException(
                            new PlayerInitException(jsonNode.get("description").asText()));

                case "IllegalCardPlacingException":
                    exceptionConnector.throwException(
                            new IllegalCardPlacingException(jsonNode.get("description")
                                                                    .asText()));

                case "IllegalPickActionException":
                    exceptionConnector.throwException(
                            new IllegalPickActionException(jsonNode.get("description")
                                                                   .asText()));
                case "NotInHandException":
                    exceptionConnector.throwException(
                            new NotInHandException(jsonNode.get("description").asText()));

                case "EmptyDeckException":
                    exceptionConnector.throwException(
                            new EmptyDeckException(jsonNode.get("description").asText()));

                case "NumOfPlayersException":
                    exceptionConnector.throwException(
                            new NumOfPlayersException(jsonNode.get("description").asText()));
                case "NotGodPlayerException":
                    exceptionConnector.throwException(
                            new NotGodPlayerException(jsonNode.get("description").asText()));

                case "GameStatusException":
                    exceptionConnector.throwException(new GameStatusException(jsonNode.get(
                            "description").asText()));
                case "NotSetNumOfPlayerException":
                    exceptionConnector.throwException(
                            new NotSetNumOfPlayerException(jsonNode.get("description")
                                                                   .asText()));
                case "IllegalPlateauActionException":
                    exceptionConnector.throwException(
                            new IllegalPlateauActionException(jsonNode.get("description")
                                                                      .asText()));
                case "MaxHandSizeException":
                    exceptionConnector.throwException(
                            new MaxHandSizeException(jsonNode.get("description").asText()));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
