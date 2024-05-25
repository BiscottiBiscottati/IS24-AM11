package it.polimi.ingsw.am11.network;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.Position;


//This is the interface used by the view to send commands to the server
//It should be initialized by the implementation of ClientNetworkHandler
public interface ClientGameConnector {

    void setStarterCard(boolean isRetro);

    void setPersonalObjective(int cardId);

    void placeCard(Position pos, int cardId, boolean isRetro);

    void drawCard(boolean fromVisible, PlayableCardType type, int cardId);

    void setNumOfPlayers(int numOfPlayers);

    void setNickname(String nickname);
}
