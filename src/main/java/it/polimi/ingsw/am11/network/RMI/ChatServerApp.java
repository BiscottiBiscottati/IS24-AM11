package it.polimi.ingsw.am11.network.RMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ChatServerApp extends UnicastRemoteObject implements ChatServer {


    private final List<ChatClient> chatClients;

    /**
     * Creates and exports a new UnicastRemoteObject object using an anonymous port.
     *
     * <p>The object is exported with a server socket
     * created using the {@link RMISocketFactory} class.
     *
     * @throws RemoteException if failed to export object
     * @since 1.1
     */
    public ChatServerApp() throws RemoteException {
        this.chatClients = new ArrayList<>();
    }

    public static void main(String[] args) {
        try {
            new ChatServerApp().startServer();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void startServer() throws RemoteException {
        // Bind the remote object's stub in the registry
        //DO NOT CALL Registry registry = LocateRegistry.getRegistry();
        Registry registry = LocateRegistry.createRegistry(Settings.PORT);
        try {
            registry.bind("ChatService", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Server ready");
    }


    @Override
    public void login(ChatClient cc) throws RemoteException {
        this.chatClients.add(cc);

    }

    @Override
    public void send(String msg) throws RemoteException {
        System.out.println("server received: " + msg);
        for (ChatClient cc : chatClients) {
            cc.receive(msg);
        }

    }

    public class Settings {
        static int PORT = 1234;
        static String SERVER_NAME = "127.0.0.1";
    }
}
