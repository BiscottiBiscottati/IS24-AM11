package it.polimi.ingsw.am11.network.socket.server.game;

import com.fasterxml.jackson.databind.JsonNode;
import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.network.socket.MessageReceiver;
import it.polimi.ingsw.am11.network.socket.utils.ContextJSON;
import it.polimi.ingsw.am11.network.socket.utils.JsonFactory;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The class that receives the game messages from the client regarding the model, if the message
 * contains an exception, it sends it to the client with a {@link ServerExceptionSender} to send the
 * exceptions to the client.
 */
public class ServerGameReceiver implements MessageReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerGameReceiver.class);

    private final @NotNull VirtualPlayerView playerView;
    private final @NotNull ServerExceptionSender serverExceptionSender;

    public ServerGameReceiver(@NotNull VirtualPlayerView playerView,
                              @NotNull ServerExceptionSender exceptionSender) {
        this.playerView = playerView;
        this.serverExceptionSender = exceptionSender;
    }

    /**
     * Receives the nickname from the client and returns it as a string
     *
     * @param message the message received from the client
     */
    public static @Nullable String receiveNickname(@NotNull String message) {
        LOGGER.info("SERVER TCP: Received message: {}", message);
        try {
            JsonNode jsonNode = JsonFactory.toJsonNode(message);
            if (! jsonNode.get("context").asText().equals(ContextJSON.GAME.toString())) return "";
            if (! jsonNode.get("method").asText().equals("setNickname")) return "";
            return jsonNode.get("nickname").asText();
        } catch (IOException e) {
            LOGGER.error("SERVER TCP: Error while parsing nickname message: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Receives the message from the client and calls the appropriate method of the player view. If
     * the method throws an exception, it sends the exception to the client with the
     * ServerExceptionSender
     *
     * @param jsonNode the message received from the client
     */
    public void receive(@NotNull JsonNode jsonNode) {
        try {
            switch (jsonNode.get("method").asText()) {
                case "setStarterCard" ->
                        playerView.setStarterCard(jsonNode.get("isRetro").asBoolean());
                case "setPersonalObjective" ->
                        playerView.setObjectiveCard(jsonNode.get("cardId").asInt());
                case "placeCard" -> playerView.placeCard(jsonNode.get("cardId").asInt(),
                                                         jsonNode.get("x").asInt(),
                                                         jsonNode.get("y").asInt(),
                                                         jsonNode.get("isRetro").asBoolean());
                case "drawCard" -> playerView.drawCard(jsonNode.get("fromVisible").asBoolean(),
                                                       PlayableCardType.valueOf(
                                                               jsonNode.get("type").asText()),
                                                       jsonNode.get("cardId").asInt());
                case "setNumOfPlayers" -> playerView.setNumOfPlayers(jsonNode.get("numOfPlayers")
                                                                             .asInt());
                case "syncMeUp" -> playerView.syncMeUp();
            }

        } catch (IllegalPlayerSpaceActionException | TurnsOrderException | PlayerInitException |
                 IllegalCardPlacingException | IllegalPickActionException | NotInHandException |
                 EmptyDeckException | IllegalPlateauActionException | MaxHandSizeException |
                 GameStatusException | NumOfPlayersException | NotGodPlayerException e) {
            LOGGER.info("SERVER TCP: Exception to send: {}", e.getMessage());
            serverExceptionSender.exception(e);
        }
    }

}
