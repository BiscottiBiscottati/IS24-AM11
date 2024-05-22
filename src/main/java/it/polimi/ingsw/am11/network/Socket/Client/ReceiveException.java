package it.polimi.ingsw.am11.network.Socket.Client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.view.client.ExceptionConnector;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiveException {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveException.class);

    private final ObjectMapper mapper;
    private final ExceptionConnector exceptionConnector;

    public ReceiveException(@NotNull ExceptionConnector exceptionConnector) {
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
                    break;
                case "TurnsOrderException":
                    exceptionConnector.throwException(
                            new TurnsOrderException(jsonNode.get("description").asText()));
                    break;

                case "PlayerInitException":
                    exceptionConnector.throwException(
                            new PlayerInitException(jsonNode.get("description").asText()));
                    break;

                case "IllegalCardPlacingException":
                    exceptionConnector.throwException(
                            new IllegalCardPlacingException(jsonNode.get("description")
                                                                    .asText()));
                    break;

                case "IllegalPickActionException":
                    exceptionConnector.throwException(
                            new IllegalPickActionException(jsonNode.get("description")
                                                                   .asText()));
                    break;
                case "NotInHandException":
                    exceptionConnector.throwException(
                            new NotInHandException(jsonNode.get("description").asText()));
                    break;

                case "EmptyDeckException":
                    exceptionConnector.throwException(
                            new EmptyDeckException(jsonNode.get("description").asText()));
                    break;

                case "NumOfPlayersException":
                    exceptionConnector.throwException(
                            new NumOfPlayersException(jsonNode.get("description").asText()));
                    break;
                case "NotGodPlayerException":
                    exceptionConnector.throwException(
                            new NotGodPlayerException(jsonNode.get("description").asText()));
                    break;

                case "GameStatusException":
                    exceptionConnector.throwException(new GameStatusException(jsonNode.get(
                            "description").asText()));
                    break;
                case "NotSetNumOfPlayerException":
                    exceptionConnector.throwException(
                            new NotSetNumOfPlayerException(jsonNode.get("description")
                                                                   .asText()));
                    break;
                case "IllegalPlateauActionException":
                    exceptionConnector.throwException(
                            new IllegalPlateauActionException(jsonNode.get("description")
                                                                      .asText()));
                    break;
                case "MaxHandSizeException":
                    exceptionConnector.throwException(
                            new MaxHandSizeException(jsonNode.get("description").asText()));
                    break;

            }
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while parsing the message", e);
        }
    }

    public void sendDisconnectionException() {
        exceptionConnector
                .throwException(new LostConnectionException("Lost connection from server"));
    }
}
