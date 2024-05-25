package it.polimi.ingsw.am11.network;

//The implementation of this interface is used to connect to the server,
//suggestion: the constructor receive as arguments the info necessary for the connection
//for example ip and port, and the clientViewUpdater that will modify the view
public interface ClientNetworkHandler {
    ClientGameConnector getConnector();

    void close();
}
