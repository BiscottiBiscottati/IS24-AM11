package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.exceptions.*;
import it.polimi.ingsw.am11.players.CardContainer;
import it.polimi.ingsw.am11.players.PlayerColor;
import it.polimi.ingsw.am11.players.Position;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface GameModel {
    //region GetterGameStatus
    List<String> getPlayers();

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
    Optional<Color> getResourceDeckTop();

    Optional<Color> getGoldDeckTop();

    List<Integer> getCommonObjectives();

    List<Integer> getExposedGoldsCrd();

    List<Integer> getExposedResourcesCrd();
    //endregion

    //region GetterPlateau
    int getPlayerPoints(String nickname)
    throws IllegalPlateauActionException;

    boolean isArmageddonTime();

    int getPlayerFinishingPosition(String nickname);

    List<String> getWinner();

    //endregion

    //region GameInitialization
    void initGame() throws IllegalNumOfPlayersException, GameStatusException;

    void addPlayerToTable(String nickname, PlayerColor colour) throws PlayerInitException, GameStatusException;

    void removePlayer(@NotNull String nickname) throws GameStatusException;

    void shufflePlayers() throws GameStatusException;

    void setStartingPlayer() throws GameStatusException;

    int pickStarter() throws EmptyDeckException, GameStatusException;

    int pickObjective() throws EmptyDeckException, GameStatusException;

    void setStarterFor(String nickname, int cardID, boolean isRetro)
    throws IllegalCardPlacingException, GameStatusException;

    void setObjectiveFor(String nickname, int cardID)
    throws IllegalPlayerSpaceActionException, GameStatusException;
    //endregion

    //region TurnsActions

    void goNextTurn() throws GameBreakingException, GameStatusException;

    void placeCard(String Nickname, int ID, Position position, boolean isRetro)
    throws IllegalCardPlacingException,
           TurnsOrderException,
           IllegalPlateauActionException, GameStatusException, NotInHandException;

    int drawFromGoldDeck(String nickname)
    throws GameBreakingException,
           EmptyDeckException,
           IllegalPlayerSpaceActionException, TurnsOrderException, GameStatusException;

    int drawFromResourceDeck(String nickname)
    throws GameBreakingException,
           EmptyDeckException,
           IllegalPlayerSpaceActionException, TurnsOrderException, GameStatusException;

    void drawVisibleGold(String nickname, int ID)
    throws GameBreakingException,
           IllegalPickActionException,
           IllegalPlayerSpaceActionException, TurnsOrderException, GameStatusException;

    void drawVisibleResource(String nickname, int ID)
    throws GameBreakingException,
           IllegalPickActionException,
           IllegalPlayerSpaceActionException, TurnsOrderException, GameStatusException;

    //TODO add possibility of signaling how many objectives a player has completed
    void countObjectivesPoints() throws IllegalPlateauActionException, GameStatusException;

    //DONE
    void endGame();
    //endregion

}
