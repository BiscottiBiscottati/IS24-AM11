package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.model.GameModel;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.model.players.utils.Position;

public class CardController {
    private final GameModel model;

    public CardController(GameModel model) {
        this.model = model;
    }

    public void drawVisibleOf(PlayableCardType type, String nickname, int cardID)
    throws GameStatusException,
           TurnsOrderException,
           GameBreakingException,
           IllegalPlayerSpaceActionException,
           IllegalPickActionException,
           PlayerInitException {
        model.drawVisibleOf(type, nickname, cardID);
    }

    public void setObjectiveFor(String nickname, int cardID)
    throws GameStatusException,
           PlayerInitException,
           IllegalPlayerSpaceActionException {
        model.setObjectiveFor(nickname, cardID);
    }

    public void placeCard(String Nickname, int ID, Position position, boolean isRetro)
    throws GameStatusException,
           PlayerInitException,
           IllegalCardPlacingException,
           TurnsOrderException,
           IllegalPlateauActionException,
           NotInHandException {
        model.placeCard(Nickname, ID, position, isRetro);
    }

    public int drawFromDeckOf(PlayableCardType type, String nickname)
    throws GameStatusException,
           TurnsOrderException,
           GameBreakingException,
           EmptyDeckException,
           IllegalPlayerSpaceActionException,
           PlayerInitException,
           MaxHandSizeException,
           IllegalPickActionException {
        return model.drawFromDeckOf(type, nickname);
    }


    public void setStarterFor(String nickname, boolean isRetro)
    throws GameStatusException,
           PlayerInitException,
           IllegalCardPlacingException {
        model.setStarterFor(nickname, isRetro);
    }
}
