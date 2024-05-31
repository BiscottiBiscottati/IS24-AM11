package it.polimi.ingsw.am11.network.socket.client.game;

import com.fasterxml.jackson.databind.JsonNode;
import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.network.socket.MessageReceiver;
import it.polimi.ingsw.am11.view.client.ExceptionThrower;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record ClientExceptionReceiver(@NotNull ExceptionThrower exceptionThrower)
        implements MessageReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientExceptionReceiver.class);

    public void receive(@NotNull JsonNode jsonNode) {
        LOGGER.debug("CLIENT TCP: Received exception: {}", jsonNode);
        // Switch on the method
        switch (jsonNode.get("method").asText()) {
            case "IllegalPlayerSpaceActionException":
                exceptionThrower.throwException(new IllegalPlayerSpaceActionException(
                        jsonNode.get("description")
                                .asText()));
                break;
            case "TurnsOrderException":
                exceptionThrower.throwException(
                        new TurnsOrderException(jsonNode.get("description").asText()));
                break;

            case "PlayerInitException":
                exceptionThrower.throwException(
                        new PlayerInitException(jsonNode.get("description").asText()));
                break;

            case "IllegalCardPlacingException":
                exceptionThrower.throwException(
                        new IllegalCardPlacingException(jsonNode.get("description")
                                                                .asText()));
                break;

            case "IllegalPickActionException":
                exceptionThrower.throwException(
                        new IllegalPickActionException(jsonNode.get("description")
                                                               .asText()));
                break;
            case "NotInHandException":
                exceptionThrower.throwException(
                        new NotInHandException(jsonNode.get("description").asText()));
                break;

            case "EmptyDeckException":
                exceptionThrower.throwException(
                        new EmptyDeckException(jsonNode.get("description").asText()));
                break;

            case "NumOfPlayersException":
                exceptionThrower.throwException(
                        new NumOfPlayersException(jsonNode.get("description").asText()));
                break;
            case "NotGodPlayerException":
                exceptionThrower.throwException(
                        new NotGodPlayerException(jsonNode.get("description").asText()));
                break;

            case "GameStatusException":
                exceptionThrower.throwException(new GameStatusException(jsonNode.get(
                        "description").asText()));
                break;
            case "NotSetNumOfPlayerException":
                exceptionThrower.throwException(
                        new NotSetNumOfPlayerException(jsonNode.get("description")
                                                               .asText()));
                break;
            case "IllegalPlateauActionException":
                exceptionThrower.throwException(
                        new IllegalPlateauActionException(jsonNode.get("description")
                                                                  .asText()));
                break;
            case "MaxHandSizeException":
                exceptionThrower.throwException(
                        new MaxHandSizeException(jsonNode.get("description").asText()));
                break;

        }
    }
}
