package it.polimi.ingsw.am11.network.RMI;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.Position;

public interface CardControllerInterface {
    void setObjectiveFor(String nickname, int cardID) throws Exception;

    void placeCard(String Nickname, int ID, Position position, boolean isRetro) throws Exception;

    int drawCard(boolean fromVisible, PlayableCardType type, String nickname, int cardID)
    throws Exception;

    void setStarterFor(String nickname, boolean isRetro) throws Exception;
}
