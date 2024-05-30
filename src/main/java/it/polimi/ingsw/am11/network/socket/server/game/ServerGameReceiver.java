package it.polimi.ingsw.am11.network.socket.server.game;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.network.socket.MessageReceiver;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ServerGameReceiver implements MessageReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerGameReceiver.class);

    private final @NotNull VirtualPlayerView playerView;
    private final @NotNull ServerExceptionSender serverExceptionSender;

    public ServerGameReceiver(@NotNull VirtualPlayerView playerView,
                              @NotNull ServerExceptionSender exceptionSender) {
        this.playerView = playerView;
        this.serverExceptionSender = exceptionSender;
    }

    public static @Nullable String receiveNickname(@NotNull String message) {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(message);
            if (! jsonNode.get("method").asText().equals("setNickname")) return null;
            return jsonNode.get("nickname").asText();
        } catch (IOException e) {
            LOGGER.error("SERVER TCP: Error while parsing nickname message: {}", e.getMessage());
            return null;
        }
    }

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
                case "setNumOfPlayers" -> {
                    playerView.setNumOfPlayers(jsonNode.get("numOfPlayers").asInt());
                }
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
