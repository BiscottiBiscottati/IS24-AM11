package it.polimi.ingsw.am11.network.RMI.Client;

import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.ClientGameConnector;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.ClientGameUpdatesInterface;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.ServerGameCommandsInterface;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.ServerLoggable;
import it.polimi.ingsw.am11.view.client.ExceptionConnector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NetworkConnector implements ClientGameConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkConnector.class);

    private final ClientRMI main;
    private final Registry registry;
    private final ClientGameUpdatesInterface remoteGameCommands;
    private final ExceptionConnector exceptionConnector;
    private final ExecutorService commandExecutor;
    private String nickname;
    private Future<?> future;

    public NetworkConnector(@NotNull ClientRMI main,
                            @NotNull Registry registry,
                            @NotNull ClientGameUpdatesInterface remoteGameCommands,
                            @NotNull ExceptionConnector exceptionConnector) {
        this.main = main;
        this.registry = registry;
        this.remoteGameCommands = remoteGameCommands;
        this.exceptionConnector = exceptionConnector;
        this.commandExecutor = Executors.newSingleThreadExecutor();
        this.nickname = null;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void setNickname(@NotNull String nickname) {
        this.nickname = nickname;

        LOGGER.debug("CLIENT RMI: Sending login request to server");
        Future<?> tempFuture = commandExecutor.submit(() -> {
            try {
                ((ServerLoggable) registry.lookup("Loggable"))
                        .login(nickname, remoteGameCommands);
                main.setHeartbeatNickname(nickname);
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            } catch (NumOfPlayersException e) {
                exceptionConnector.throwException(e);
            } catch (NotSetNumOfPlayerException e) {
                exceptionConnector.throwException(e);
            } catch (GameStatusException e) {
                exceptionConnector.throwException(e);
            } catch (PlayerInitException e) {
                exceptionConnector.throwException(e);
            } catch (RemoteException e) {
                LOGGER.error("CLIENT RMI: Connection error", e);
            }
        });
        synchronized (commandExecutor) {
            future = tempFuture;
        }

    }

    @Override
    public void setStarterCard(boolean isRetro) {
        LOGGER.debug("CLIENT RMI: Sending setStarterCard request to server");

        checkIfNickSet();

        Future<?> tempFuture = commandExecutor.submit(() -> {
            try {
                ((ServerGameCommandsInterface) registry.lookup("PlayerView"))
                        .setStarterCard(nickname, isRetro);
            } catch (NotBoundException | RemoteException e) {
                throw new RuntimeException(e); // TODO disconnection from server
            } catch (PlayerInitException e) {
                exceptionConnector.throwException(e);
            } catch (IllegalCardPlacingException e) {
                exceptionConnector.throwException(e);
            } catch (GameStatusException e) {
                exceptionConnector.throwException(e);
            }
        });
        synchronized (commandExecutor) {
            future = tempFuture;
        }
    }

    private void checkIfNickSet() {
        if (nickname == null) {
            throw new RuntimeException("Nickname not set");
        }
    }

    @Override
    public void setPersonalObjective(int cardId) {
        LOGGER.debug("CLIENT RMI: Sending setObjectiveCard request to server");

        checkIfNickSet();

        Future<?> tempFuture = commandExecutor.submit(() -> {
            try {
                ((ServerGameCommandsInterface) registry.lookup("PlayerView"))
                        .setObjectiveCard(nickname, cardId);
            } catch (NotBoundException | RemoteException e) {
                throw new RuntimeException(e);
            } catch (IllegalPlayerSpaceActionException e) {
                exceptionConnector.throwException(e);
            } catch (PlayerInitException e) {
                exceptionConnector.throwException(e);
            } catch (GameStatusException e) {
                exceptionConnector.throwException(e);
            }
        });
        synchronized (commandExecutor) {
            future = tempFuture;
        }
    }

    @Override
    public void placeCard(@NotNull Position pos, int cardId, boolean isRetro) {
        LOGGER.debug("CLIENT RMI: Sending placeCard request to server");

        checkIfNickSet();

        Future<?> tempFuture = commandExecutor.submit(() -> {
            try {
                ((ServerGameCommandsInterface) registry.lookup("PlayerView"))
                        .placeCard(nickname, cardId, pos.x(), pos.y(), isRetro);
            } catch (NotBoundException | RemoteException e) {
                throw new RuntimeException(e);
            } catch (TurnsOrderException e) {
                exceptionConnector.throwException(e);
            } catch (PlayerInitException e) {
                exceptionConnector.throwException(e);
            } catch (IllegalCardPlacingException e) {
                exceptionConnector.throwException(e);
            } catch (NotInHandException e) {
                exceptionConnector.throwException(e);
            } catch (IllegalPlateauActionException e) {
                exceptionConnector.throwException(e);
            } catch (GameStatusException e) {
                exceptionConnector.throwException(e);
            }
        });
        synchronized (commandExecutor) {
            future = tempFuture;
        }
    }

    @Override
    public void drawCard(boolean fromVisible, PlayableCardType type, int cardId) {
        LOGGER.debug("CLIENT RMI: Sending drawCard request to server");

        checkIfNickSet();

        Future<?> tempFuture = commandExecutor.submit(() -> {
            try {
                ((ServerGameCommandsInterface) registry.lookup("PlayerView"))
                        .drawCard(nickname, fromVisible, type, cardId);
            } catch (NotBoundException | RemoteException e) {
                throw new RuntimeException(e);
            } catch (IllegalPlayerSpaceActionException e) {
                exceptionConnector.throwException(e);
            } catch (TurnsOrderException e) {
                exceptionConnector.throwException(e);
            } catch (IllegalPickActionException e) {
                exceptionConnector.throwException(e);
            } catch (PlayerInitException e) {
                exceptionConnector.throwException(e);
            } catch (EmptyDeckException e) {
                exceptionConnector.throwException(e);
            } catch (MaxHandSizeException e) {
                exceptionConnector.throwException(e);
            } catch (GameStatusException e) {
                exceptionConnector.throwException(e);
            }
        });
        synchronized (commandExecutor) {
            future = tempFuture;
        }
    }

    @Override
    public void setNumOfPlayers(int numOfPlayers) {
        LOGGER.debug("CLIENT RMI: Sending setNumOfPlayers request to server");

        checkIfNickSet();

        Future<?> tempFuture = commandExecutor.submit(() -> {
            try {
                ((ServerLoggable) registry.lookup("Loggable"))
                        .setNumOfPlayers(nickname, numOfPlayers);
            } catch (NotBoundException | RemoteException e) {
                throw new RuntimeException(e); // FIXME should check for connection issues
            } catch (NumOfPlayersException e) {
                exceptionConnector.throwException(e);
            } catch (GameStatusException e) {
                exceptionConnector.throwException(e);
            } catch (NotGodPlayerException e) {
                exceptionConnector.throwException(e);
            }
        });
        synchronized (commandExecutor) {
            future = tempFuture;
        }

    }

    // TODO we may delete this
    @TestOnly
    public void await() {
        synchronized (commandExecutor) {
            if (future != null) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
