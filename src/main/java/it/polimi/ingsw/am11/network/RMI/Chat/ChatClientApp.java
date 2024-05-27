package it.polimi.ingsw.am11.network.RMI.Chat;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class ChatClientApp extends UnicastRemoteObject implements ChatClient {

    private ChatServer cs;

    /**
     * Creates and exports a new UnicastRemoteObject object using an anonymous port.
     *
     * <p>The object is exported with a server socket
     * created using the {@link RMISocketFactory} class.
     *
     * @throws RemoteException if failed to export object
     * @since 1.1
     */
    protected ChatClientApp() throws RemoteException {
    }

    public static void main(String[] args) {
        try {
            new ChatClientApp().startClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startClient() throws IOException, NotBoundException {
        // Getting the registry
        Registry registry;
        registry = LocateRegistry.getRegistry(Settings.SERVER_NAME,
                                              Settings.PORT);
        // Looking up the registry for the remote object
        this.cs = (ChatServer) registry.lookup("ChatService");
        this.cs.login(this);
        inputLoop();
    }

    void inputLoop() throws IOException {
        BufferedReader br = new BufferedReader(new java.io.InputStreamReader(System.in));
        String msg;

        while ((msg = br.readLine()) != null) {
            this.cs.send(msg);
        }
    }

    @Override
    public void receive(String msg) throws RemoteException {
        System.out.println(msg);
    }

    public class Settings {
        static int PORT = 1234;
        static @NotNull String SERVER_NAME = "127.0.0.1";
    }
}
