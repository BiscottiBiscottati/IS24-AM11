package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.model.players.utils.CardContainer;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.view.events.listeners.PlayerListener;
import it.polimi.ingsw.am11.view.events.listeners.TableListener;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings({"ClassWithTooManyMethods", "OverlyCoupledClass"})
public interface GameModel {

    RuleSet getRuleSet();

    Set<String> getPlayers();

    String getCurrentTurnPlayer() throws GameStatusException;

    String getFirstPlayer() throws GameStatusException;

    Set<Integer> getPlayerHand(String nickname) throws GameStatusException, PlayerInitException;

    Set<Integer> getPlayerObjective(String nickname) throws PlayerInitException,
                                                            GameStatusException;

    PlayerColor getPlayerColor(String nickname) throws PlayerInitException;

    //FIXME this interface has to be changed, it can't return a map
    Map<Position, CardContainer> getPositionedCard(String nickname)
    throws PlayerInitException, GameStatusException;

    Set<Position> getAvailablePositions(String nickname)
    throws PlayerInitException, GameStatusException;

    @NotNull
    Set<Integer> getCommonObjectives() throws GameStatusException;

    Set<Integer> getExposedCards(PlayableCardType type) throws GameStatusException;


    int getPlayerPoints(String nickname)
    throws PlayerInitException, GameStatusException,
           GameBreakingException;

    int getPlayerFinishingPosition(String nickname)
    throws PlayerInitException, GameStatusException,
           GameBreakingException;

    Set<String> getWinner() throws GameStatusException;

    void initGame() throws NumOfPlayersException, GameStatusException, GameBreakingException;

    void addPlayerToTable(String nickname, PlayerColor colour)
    throws PlayerInitException, GameStatusException, NumOfPlayersException;

    void removePlayer(@NotNull String nickname) throws GameStatusException;

    void setStarterFor(String nickname, boolean isRetro)
    throws IllegalCardPlacingException, GameStatusException, PlayerInitException,
           GameBreakingException;

    void setObjectiveFor(String nickname, int cardID)
    throws IllegalPlayerSpaceActionException, GameStatusException, PlayerInitException;

    void placeCard(String Nickname, int ID, Position position, boolean isRetro)
    throws IllegalCardPlacingException,
           TurnsOrderException,
           IllegalPlateauActionException, GameStatusException, NotInHandException,
           PlayerInitException;

    int drawFromDeckOf(PlayableCardType type, String nickname)
    throws GameStatusException, TurnsOrderException, GameBreakingException, EmptyDeckException,
           IllegalPlayerSpaceActionException, PlayerInitException, MaxHandSizeException,
           IllegalPickActionException;

    void goNextTurn() throws GameBreakingException, GameStatusException;

    int drawVisibleOf(PlayableCardType type, String nickname, int cardID)
    throws GameStatusException, TurnsOrderException, GameBreakingException,
           IllegalPlayerSpaceActionException, IllegalPickActionException, PlayerInitException;

    void forceEnd();

    GameStatus getStatus();

    Optional<Color> getDeckTop(PlayableCardType type) throws GameStatusException;

    Set<Integer> getCandidateObjectives(@NotNull String nickname)
    throws PlayerInitException, GameStatusException;

    Optional<Integer> getStarterCard(@NotNull String nickname)
    throws PlayerInitException, GameStatusException;

    void addPlayerListener(String nickname, PlayerListener playerListener);

    void addTableListener(TableListener listener);

    void addUnavailablePlayer(String nickname);

    void playerIsNowAvailable(String nickname);
}
