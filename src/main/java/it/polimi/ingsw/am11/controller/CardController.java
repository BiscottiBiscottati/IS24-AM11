package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.model.GameModel;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.RMI.CardControllerInterface;

public class CardController implements CardControllerInterface {
    private final GameModel model;

    public CardController(GameModel model) {
        this.model = model;
    }

    public void setObjectiveFor(String nickname, int cardID)
    throws GameStatusException,
           PlayerInitException,
           IllegalPlayerSpaceActionException {
        model.setObjectiveFor(nickname, cardID);
    }

    public void placeCard(String Nickname, int cardId, Position position, boolean isRetro)
    throws GameStatusException,
           PlayerInitException,
           IllegalCardPlacingException,
           TurnsOrderException,
           IllegalPlateauActionException,
           NotInHandException {
        model.placeCard(Nickname, cardId, position, isRetro);
    }

    public int drawCard(boolean fromVisible, PlayableCardType type, String nickname, int cardID)
    throws IllegalPlayerSpaceActionException, TurnsOrderException, IllegalPickActionException,
           PlayerInitException, GameStatusException, EmptyDeckException,
           MaxHandSizeException {
        try {
            if (fromVisible) {
                return model.drawVisibleOf(type, nickname, cardID);
            } else return model.drawFromDeckOf(type, nickname);
        } catch (GameBreakingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setStarterFor(String nickname, boolean isRetro)
    throws GameStatusException,
           PlayerInitException,
           IllegalCardPlacingException {
        model.setStarterFor(nickname, isRetro);
    }
}
