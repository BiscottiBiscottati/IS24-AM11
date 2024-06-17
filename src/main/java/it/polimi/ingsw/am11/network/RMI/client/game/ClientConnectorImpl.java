package it.polimi.ingsw.am11.network.RMI.client.game;

import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.RMI.client.ClientRMI;
import it.polimi.ingsw.am11.network.RMI.client.chat.ClientChatConnectorImpl;
import it.polimi.ingsw.am11.network.RMI.remote.ServerLoggable;
import it.polimi.ingsw.am11.network.RMI.remote.chat.ClientChatInterface;
import it.polimi.ingsw.am11.network.RMI.remote.game.ClientGameUpdatesInterface;
import it.polimi.ingsw.am11.network.RMI.remote.game.ServerGameCommandsInterface;
import it.polimi.ingsw.am11.network.connector.ClientGameConnector;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import it.polimi.ingsw.am11.view.client.ExceptionThrower;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class ClientConnectorImpl implements ClientGameConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientConnectorImpl.class);
    private static ExecutorService commandExecutor;

    private final @NotNull ClientRMI main;
    private final @NotNull Registry registry;
    private final @NotNull ClientViewUpdater updater;
    private final @NotNull ClientGameUpdatesInterface remoteGameCommands;
    private final @NotNull ClientChatInterface remoteChat;
    private final @NotNull ExceptionThrower exceptionThrower;
    private final @NotNull ClientChatConnectorImpl chatConnector;
    private final @NotNull AtomicReference<String> nickname;
    private @NotNull Future<?> future;

    public ClientConnectorImpl(@NotNull ClientRMI main,
                               @NotNull Registry registry,
                               @NotNull ClientGameUpdatesInterface remoteGameCommands,
                               @NotNull ClientChatInterface remoteChat,
                               @NotNull ClientChatConnectorImpl chatConnector,
                               @NotNull ClientViewUpdater updater) {
        this.main = main;
        this.registry = registry;
        this.remoteGameCommands = remoteGameCommands;
        this.remoteChat = remoteChat;
        this.chatConnector = chatConnector;
        this.updater = updater;
        this.exceptionThrower = updater.getExceptionThrower();
        this.nickname = new AtomicReference<>(null);
        this.future = CompletableFuture.completedFuture(null);
    }

    public synchronized static void start() {
        commandExecutor = Executors.newSingleThreadExecutor();
    }

    public synchronized static void stop() {
        commandExecutor.shutdown();
    }

    public synchronized @Nullable String getNickname() {
        return nickname.get();
    }

    @Override
    public synchronized void setNickname(@NotNull String nickname) {
        try {
            future.get();
        } catch (ExecutionException | InterruptedException ignored) {}

        this.nickname.compareAndSet(null, nickname);

        LOGGER.debug("CLIENT RMI: Sending login request to server");
        future = commandExecutor.submit(() -> {
            try {
                LOGGER.debug("CLIENT RMI: Looking up Loggable and logging in");
                ((ServerLoggable) registry.lookup("Loggable"))
                        .login(nickname, remoteGameCommands, remoteChat);
                main.setHeartbeatNickname(nickname);
                chatConnector.setSender(nickname);

            } catch (NotBoundException | RemoteException e) {
                LOGGER.debug("CLIENT RMI: Connection error while logging in: {}", e.getMessage());
                updater.disconnectedFromServer(e.getMessage());
            } catch (NumOfPlayersException e) {
                exceptionThrower.throwException(e);
            } catch (NotSetNumOfPlayerException e) {
                exceptionThrower.throwException(e);
            } catch (GameStatusException e) {
                exceptionThrower.throwException(e);
            } catch (PlayerInitException e) {
                exceptionThrower.throwException(e);
            }
        });
    }

    @Override
    public synchronized void setStarterCard(boolean isRetro) {
        try {
            future.get();
        } catch (ExecutionException | InterruptedException ignored) {}

        LOGGER.debug("CLIENT RMI: Sending setStarterCard request to server");

        checkIfNickSet();

        future = commandExecutor.submit(() -> {
            try {
                ((ServerGameCommandsInterface) registry.lookup("PlayerView"))
                        .setStarterCard(nickname.get(), isRetro);
            } catch (NotBoundException | RemoteException e) {
                LOGGER.debug("CLIENT RMI: Connection error while setting starter: {}",
                             e.getMessage());
                updater.disconnectedFromServer(e.getMessage());
            } catch (PlayerInitException e) {
                //TODO to remove, controller have to deal with this
                exceptionThrower.throwException(e);
            } catch (IllegalCardPlacingException e) {
                //TODO to remove, controller have to deal with this
                exceptionThrower.throwException(e);
            } catch (GameStatusException e) {
                exceptionThrower.throwException(e);
            }
        });
    }

    @Override
    public synchronized void setPersonalObjective(int cardId) {
        try {
            future.get();
        } catch (ExecutionException | InterruptedException ignored) {}

        LOGGER.debug("CLIENT RMI: Sending setObjectiveCard request to server");

        checkIfNickSet();

        future = commandExecutor.submit(() -> {
            try {
                ((ServerGameCommandsInterface) registry.lookup("PlayerView"))
                        .setObjectiveCard(nickname.get(), cardId);
            } catch (NotBoundException | RemoteException e) {
                LOGGER.debug("CLIENT RMI: Connection error while setting objective: {}",
                             e.getMessage());
                updater.disconnectedFromServer(e.getMessage());
            } catch (IllegalPlayerSpaceActionException e) {
                exceptionThrower.throwException(e);
            } catch (PlayerInitException e) {
                //TODO to remove, controller have to deal with this
                exceptionThrower.throwException(e);
            } catch (GameStatusException e) {
                exceptionThrower.throwException(e);
            }
        });
    }

    @Override
    public synchronized void placeCard(@NotNull Position pos, int cardId, boolean isRetro) {
        try {
            future.get();
        } catch (ExecutionException | InterruptedException ignored) {}

        LOGGER.debug("CLIENT RMI: Sending placeCard request to server");

        checkIfNickSet();

        future = commandExecutor.submit(() -> {
            try {
                ((ServerGameCommandsInterface) registry.lookup("PlayerView"))
                        .placeCard(nickname.get(), cardId, pos.x(), pos.y(), isRetro);
            } catch (NotBoundException | RemoteException e) {
                LOGGER.debug("CLIENT RMI: Connection error while placing card: {}",
                             e.getMessage());
                updater.disconnectedFromServer(e.getMessage());
            } catch (TurnsOrderException e) {
                exceptionThrower.throwException(e);
            } catch (PlayerInitException e) {
                //TODO to remove, controller have to deal with this
                exceptionThrower.throwException(e);
            } catch (IllegalCardPlacingException e) {
                exceptionThrower.throwException(e);
            } catch (NotInHandException e) {
                exceptionThrower.throwException(e);
            } catch (IllegalPlateauActionException e) {
                //TODO to remove, controller have to deal with this
                exceptionThrower.throwException(e);
            } catch (GameStatusException e) {
                exceptionThrower.throwException(e);
            }
        });
    }

    @Override
    public synchronized void drawCard(boolean fromVisible, @NotNull PlayableCardType type,
                                      int cardId) {
        try {
            future.get();
        } catch (ExecutionException | InterruptedException ignored) {}

        LOGGER.debug("CLIENT RMI: Sending drawCard request to server");

        checkIfNickSet();

        future = commandExecutor.submit(() -> {
            try {
                ((ServerGameCommandsInterface) registry.lookup("PlayerView"))
                        .drawCard(nickname.get(), fromVisible, type, cardId);
            } catch (NotBoundException | RemoteException e) {
                LOGGER.debug("CLIENT RMI: Connection error while drawing card: {}",
                             e.getMessage());
                updater.disconnectedFromServer(e.getMessage());
            } catch (IllegalPlayerSpaceActionException e) {
                exceptionThrower.throwException(e);
            } catch (TurnsOrderException e) {
                exceptionThrower.throwException(e);
            } catch (IllegalPickActionException e) {
                exceptionThrower.throwException(e);
            } catch (PlayerInitException e) {
                //TODO to remove, controller have to deal with this
                exceptionThrower.throwException(e);
            } catch (EmptyDeckException e) {
                exceptionThrower.throwException(e);
            } catch (MaxHandSizeException e) {
                exceptionThrower.throwException(e);
            } catch (GameStatusException e) {
                exceptionThrower.throwException(e);
            }
        });
    }

    @Override
    public synchronized void setNumOfPlayers(int numOfPlayers) {
        try {
            future.get();
        } catch (InterruptedException | ExecutionException ignored) {}

        LOGGER.debug("CLIENT RMI: Sending setNumOfPlayers request to server");

        checkIfNickSet();

        future = commandExecutor.submit(() -> {
            try {
                ((ServerGameCommandsInterface) registry.lookup("PlayerView"))
                        .setNumOfPlayers(nickname.get(), numOfPlayers);
            } catch (NotBoundException | RemoteException e) {
                LOGGER.debug("CLIENT RMI: Connection error while setting number of players: {}",
                             e.getMessage());
                updater.disconnectedFromServer(e.getMessage());
            } catch (NumOfPlayersException e) {
                exceptionThrower.throwException(e);
            } catch (GameStatusException e) {
                exceptionThrower.throwException(e);
            } catch (NotGodPlayerException e) {
                exceptionThrower.throwException(e);
            }
        });
    }

    private void checkIfNickSet() {
        if (nickname.get() == null) {
            throw new RuntimeException("Nickname not set");
        }
    }
}
