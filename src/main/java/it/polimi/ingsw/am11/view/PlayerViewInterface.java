package it.polimi.ingsw.am11.view;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;

import java.rmi.Remote;

public interface PlayerViewInterface extends Remote {

    void setStarterCard(boolean isRetro);

    void setObjectiveCard(int cardId);

    void placeCard(int cardId, int x, int y, boolean isRetro);

    void drawCard(boolean fromVisible, PlayableCardType type, int cardId);
}
