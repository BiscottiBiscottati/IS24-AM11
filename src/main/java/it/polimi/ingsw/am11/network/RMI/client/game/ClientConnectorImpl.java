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

/**
 * This class is the implementation of the {@link ClientGameConnector} interface. It is used to send
 * commands to the server through RMI.
 * <p>
 * The class is used by the {@link ClientRMI} class to send commands to the server. The class uses a
 * {@link ClientGameUpdatesInterface} to send game updates to the server. The class uses a
 * {@link ClientChatInterface} to send chat messages to the server. The class uses a
 * {@link ClientChatConnectorImpl} to send chat messages to the server. The class uses a
 * {@link ClientViewUpdater} to update the view. The class uses a {@link ExceptionThrower} to throw
 * exceptions. The class uses an {@link AtomicReference} to store the nickname of the player. The
 * class uses a {@link Future} to store the last command sent to the server. The class uses a
 * {@link ExecutorService} to execute commands. The class uses a {@link Logger} to log messages.
 * </p>
 */
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

    /**
     * Starts the command executor.
     * <p>
     * The command executor is a single thread executor that is used to send commands to the server.
     * The executor is started when the client is created.
     * </p>
     */
    public synchronized static void start() {
        commandExecutor = Executors.newSingleThreadExecutor();
    }

    /**
     * Stops the command executor.
     */
    public synchronized static void stop() {
        commandExecutor.shutdown();
    }

    /**
     * @return The nickname of the player.
     */
    public synchronized @Nullable String getNickname() {
        return nickname.get();
    }

    /**
     * Sets the nickname of the player and sends a login request to the server.
     * <p>
     * The method sends a login request to the server. The method uses the command executor to send
     * the request. The method uses a future to store the last command sent to the server. The
     * method uses an atomic reference to store the nickname of the player.
     * </p>
     *
     * @param nickname The nickname of the player.
     */
    @Override
    public synchronized void setNickname(@NotNull String nickname) {
        try {
            future.get();
        } catch (ExecutionException | InterruptedException ignored) {}

        this.nickname.set(nickname);

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

    /**
     * Sends a syncMeUp request to the server to synchronize the client with the server.
     */
    @Override
    public void syncMeUp() {
        try {
            future.get();
        } catch (ExecutionException | InterruptedException ignored) {}

        LOGGER.debug("CLIENT RMI: Sending syncMeUp request to server");

        checkIfNickSet();

        future = commandExecutor.submit(() -> {
            try {
                ((ServerGameCommandsInterface) registry.lookup("PlayerView"))
                        .syncMeUp(nickname.get());
            } catch (NotBoundException | RemoteException e) {
                LOGGER.debug("CLIENT RMI: Connection error while syncing up: {}", e.getMessage());
                updater.disconnectedFromServer(e.getMessage());
            }
        });
    }

    /**
     * Checks if the nickname of the player is set.
     */
    private void checkIfNickSet() {
        if (nickname.get() == null) {
            throw new RuntimeException("Nickname not set");
        }
    }

    /**
     * Sends a setStarterCard request to the server to set the starter card.
     *
     * @param isRetro The retro flag.
     */
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
            } catch (GameStatusException e) {
                exceptionThrower.throwException(e);
            }
        });
    }

    /**
     * Sends a setPersonalObjective request to the server to set the personal objective card.
     *
     * @param cardId The id of the card.
     */
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
            } catch (GameStatusException e) {
                exceptionThrower.throwException(e);
            }
        });
    }

    /**
     * Sends a placeCard request to the server to place a card.
     *
     * @param pos     The position of the card.
     * @param cardId  The id of the card.
     * @param isRetro The retro flag.
     */
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
            } catch (IllegalCardPlacingException e) {
                exceptionThrower.throwException(e);
            } catch (NotInHandException e) {
                exceptionThrower.throwException(e);
            } catch (GameStatusException e) {
                exceptionThrower.throwException(e);
            }
        });
    }

    /**
     * Sends a drawCard request to the server to draw a card.
     *
     * @param fromVisible Defines if the card is drawn from the visible deck or not.
     * @param type        The type of the card.
     * @param cardId      The id of the card.
     */
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
            } catch (EmptyDeckException e) {
                exceptionThrower.throwException(e);
            } catch (MaxHandSizeException e) {
                exceptionThrower.throwException(e);
            } catch (GameStatusException e) {
                exceptionThrower.throwException(e);
            }
        });
    }

    /**
     * Sends a setNumOfPlayers request to the server to set the number of players.
     *
     * @param numOfPlayers The number of players.
     */
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
}
