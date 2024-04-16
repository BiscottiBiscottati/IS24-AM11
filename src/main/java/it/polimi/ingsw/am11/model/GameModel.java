package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.exceptions.*;
import it.polimi.ingsw.am11.players.CardContainer;
import it.polimi.ingsw.am11.players.PlayerColor;
import it.polimi.ingsw.am11.players.Position;
import it.polimi.ingsw.am11.table.GameStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface GameModel {

    Set<String> getPlayers();

    String getCurrentTurnPlayer();

    String getFirstPlayer();

    Set<Integer> getPlayerHand(String nickname);

    Set<Integer> getPlayerObjective(String nickname);

    PlayerColor getPlayerColor(String nickname);

    //FIXME this interface has to be changed, it can't return a map
    Map<Position, CardContainer> getPositionedCard(String nickname) throws PlayerInitException;

    Set<Position> getAvailablePositions(String nickname) throws PlayerInitException;

    List<Integer> getCommonObjectives();

    Set<Integer> getExposedCards(PlayableCardType type);


    int getPlayerPoints(String nickname)
    throws IllegalPlateauActionException, PlayerInitException;

    int getPlayerFinishingPosition(String nickname)
    throws IllegalPlateauActionException, PlayerInitException;

    Set<String> getWinner();

    void initGame() throws IllegalNumOfPlayersException, GameStatusException;

    void addPlayerToTable(String nickname, PlayerColor colour)
    throws PlayerInitException, GameStatusException;

    void removePlayer(@NotNull String nickname) throws GameStatusException;

    int pickStarter() throws EmptyDeckException, GameStatusException;

    int pickObjective() throws EmptyDeckException, GameStatusException;

    void setStarterFor(String nickname, int cardID, boolean isRetro)
    throws IllegalCardPlacingException, GameStatusException, PlayerInitException;

    void setObjectiveFor(String nickname, int cardID)
    throws IllegalPlayerSpaceActionException, GameStatusException, PlayerInitException;

    String goNextTurn() throws GameBreakingException, GameStatusException;

    void placeCard(String Nickname, int ID, Position position, boolean isRetro)
    throws IllegalCardPlacingException,
           TurnsOrderException,
           IllegalPlateauActionException, GameStatusException, NotInHandException,
           PlayerInitException;

    int drawFromDeckOf(PlayableCardType type, String nickname)
    throws GameStatusException, TurnsOrderException, GameBreakingException, EmptyDeckException,
           IllegalPlayerSpaceActionException, PlayerInitException, MaxHandSizeException;

    void drawVisibleOf(PlayableCardType type, String nickname, int cardID)
    throws GameStatusException, TurnsOrderException, GameBreakingException,
           IllegalPlayerSpaceActionException, IllegalPickActionException, PlayerInitException;

    void countObjectivesPoints()
    throws IllegalPlateauActionException, GameStatusException, GameBreakingException;

    void endGame();

    GameStatus getStatus();

    Optional<Color> getDeckTop(PlayableCardType type);

}
