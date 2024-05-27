package it.polimi.ingsw.am11.view.client.GUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.ClientGameConnector;
import it.polimi.ingsw.am11.network.ClientNetworkHandler;
import it.polimi.ingsw.am11.network.factory.ConnectionType;
import org.jetbrains.annotations.NotNull;

public class GuiActuator {

    private final GuiUpdater guiUpdater;
    private ClientGameConnector connector = null;

    public GuiActuator(GuiUpdater guiUpdater) {
        this.guiUpdater = guiUpdater;
    }

    public void connect(@NotNull String type, String ip, int port) throws Exception {
        ClientNetworkHandler clientHandler =
                ConnectionType.fromString(type)
                              .orElseThrow(() -> new RuntimeException(
                                      "Type is set neither to rmi nor to socket"))
                              .create(ip, port, guiUpdater);
        connector = clientHandler.getGameUpdatesInterface();
    }

    public void setName(String nick) {
        connector.setNickname(nick);
    }

    public void setNumOfPlayers(int numOfPlayers) {
        connector.setNumOfPlayers(numOfPlayers);
    }

    public void setStarterCard(boolean isRetro) {
        connector.setStarterCard(isRetro);
    }

    public void setPersonalObjective(int cardId) {
        connector.setPersonalObjective(cardId);
    }

    public void placeCard(int x, int y, int cardId, boolean isRetro) {
        connector.placeCard(new Position(x, y), cardId, isRetro);
    }

    public void drawCard(boolean fromVisible, PlayableCardType type, int cardId) {
        connector.drawCard(fromVisible, type, cardId);
    }
}
