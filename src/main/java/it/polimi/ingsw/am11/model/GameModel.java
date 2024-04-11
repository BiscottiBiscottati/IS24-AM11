package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.exceptions.*;
import it.polimi.ingsw.am11.players.CardContainer;
import it.polimi.ingsw.am11.players.PlayerColor;
import it.polimi.ingsw.am11.players.Position;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GameModel {
    //region GetterGameStatus
    List<String> getPlayerListInOrder();

    String getCurrentTurnPlayer();

    String getFirstPlayer();
    //endregion

    //region GettersPlayer
    List<Integer> getPlayerHand(String nickname);

    List<Integer> getPlayerObjective(String nickname);

    PlayerColor getPlayerColor(String nickname);

    //FIXME this interface has to be changed, it can't return a map
    Map<Position, CardContainer> getPositionedCard(String nickname);

    Set<Position> getAvailablePositions(String nickname);

    //endregion

    //region GetterPickableTable
    Color getResourceDeckTop();

    Color getGoldDeckTop();

    List<Integer> getCommonObjectives();

    List<Integer> getExposedGoldsCrd();

    List<Integer> getExposedResourcesCrd();
    //endregion

    //region GetterPlateau
    int getPlayerPoints(String nickname) throws IllegalPlateauActionException;

    boolean getIsArmageddonTime();

    int getPlayerFinishingPosition(String nickname);

    List<String> getWinner();

    //endregion

    //region GameInitialization
    void initGame() throws IllegalNumOfPlayersException;

    void addPlayerToTable(String nickname, PlayerColor colour) throws PlayerInitException;

    void shufflePlayers();

    void setStartingPlayer();

    int pickStarter();

    int pickObjective();

    void setStarterFor(String nickname, int cardID, boolean isRetro) throws IllegalCardPlacingException;

    void setObjectiveFor(String nickname, int cardID) throws IllegalPlayerSpaceActionException;
    //endregion

    //region TurnsActions

    void goNextTurn();

    void placeCard(String Nickname, int ID, Position position, boolean isRetro) throws IllegalCardPlacingException, TurnsOrderException;

    int drawFromGoldDeck(String nickname);

    int drawFromResourceDeck(String nickname);

    void drawVisibleGold(String nickname, int ID);

    void drawVisibleResource(String nickname, int ID);
    //endregion

}
