package it.polimi.ingsw.am11.network.RMI.Client;

import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.network.ClientNetworkHandler;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.ClientGameUpdatesInterface;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.ServerGameCommandsInterface;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.ServerLoggable;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import it.polimi.ingsw.am11.view.client.ExceptionConnector;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ClientRMI implements ClientNetworkHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRMI.class);

    private final ExecutorService commandExecutor;
    private final Registry registry;
    private final ClientGameUpdatesInterface connector;
    private final NetworkConnector nConnector;
    private final @NotNull ExceptionConnector exceptionConnector;
    private Future<?> future;

    public ClientRMI(String ip, int port, ClientViewUpdater updater) throws RemoteException {

        commandExecutor = Executors.newSingleThreadExecutor();
        future = null;

        // Getting the registry
        registry = LocateRegistry.getRegistry(ip, port);
        LOGGER.debug("CLIENT RMI: Registry located {}", registry.toString());
        // Check if the connection is working
        registry.list();
        // Looking up the registry for the remote object
        this.nConnector = new NetworkConnector(this);
        ClientGameUpdatesImpl clientObject = new ClientGameUpdatesImpl(updater);
        connector = (ClientGameUpdatesInterface) UnicastRemoteObject.exportObject(clientObject, 0);

        exceptionConnector = updater.getExceptionConnector();
    }

    public void login(String nick)
    throws RemoteException {
        LOGGER.debug("CLIENT RMI: Sending login request to server");
        Future<?> tempFuture = commandExecutor.submit(() -> {
            try {
                ((ServerLoggable) registry.lookup("Loggable")).login(nick, connector);
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

    public void setNumOfPlayers(String nick, int numOfPlayers) throws RemoteException {
        LOGGER.debug("CLIENT RMI: Sending setNumOfPlayers request to server");

        Future<?> tempFuture = commandExecutor.submit(() -> {
            try {
                ((ServerLoggable) registry.lookup("Loggable")).setNumOfPlayers(nick, numOfPlayers);
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

    public synchronized void logout(String nick) throws RemoteException {
        ServerLoggable stub1;
        try {
            stub1 = (ServerLoggable) registry.lookup("Loggable");
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
        stub1.logout(nick);
    }

    public void setStarterCard(String nick, boolean isRetro) throws RemoteException {
        LOGGER.debug("CLIENT RMI: Sending setStarterCard request to server");

        Future<?> tempFuture = commandExecutor.submit(() -> {
            try {
                ((ServerGameCommandsInterface) registry.lookup("PlayerView")).setStarterCard(nick,
                                                                                             isRetro);
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

    public void setObjectiveCard(String nick, int cardId) throws RemoteException {
        LOGGER.debug("CLIENT RMI: Sending setObjectiveCard request to server");

        Future<?> tempFuture = commandExecutor.submit(() -> {
            try {
                ((ServerGameCommandsInterface) registry.lookup("PlayerView"))
                        .setObjectiveCard(nick, cardId);
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

    public void placeCard(String nick, int cardId, int x, int y, boolean isRetro)
    throws RemoteException {
        LOGGER.debug("CLIENT RMI: Sending placeCard request to server");

        Future<?> tempFuture = commandExecutor.submit(() -> {
            try {
                ((ServerGameCommandsInterface) registry.lookup("PlayerView"))
                        .placeCard(nick, cardId, x, y, isRetro);
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

    public void drawCard(String nick, boolean fromVisible, PlayableCardType type,
                         int cardId)
    throws RemoteException {
        LOGGER.debug("CLIENT RMI: Sending drawCard request to server");

        Future<?> tempFuture = commandExecutor.submit(() -> {
            try {
                ((ServerGameCommandsInterface) registry.lookup("PlayerView"))
                        .drawCard(nick, fromVisible, type, cardId);
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

    public NetworkConnector getConnector() {
        return nConnector;
    }

    @Override
    public void close() {
        try {
            commandExecutor.shutdown();
            await();
            UnicastRemoteObject.unexportObject(connector, true);
        } catch (NoSuchObjectException e) {
            LOGGER.error("CLIENT RMI: No Connector to un-export");
        }
        LOGGER.debug("CLIENT RMI: Client closed");
    }

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

    public void reconnect(String nick) throws RemoteException {
        ServerLoggable stub1;
        try {
            stub1 = (ServerLoggable) registry.lookup("Loggable");
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
        stub1.reconnect(nick);
    }
}
