package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.chat.ClientChatController;
import it.polimi.ingsw.am11.network.ChatCltToNetConnector;
import it.polimi.ingsw.am11.network.CltToNetConnector;
import it.polimi.ingsw.am11.view.client.TUI.states.Connecting;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;

public class TuiHandler {

    private final MiniGameModel model;

    private final TuiUpdater tuiUpdater;
    private CltToNetConnector connector;
    private ClientChatController chatController;
    private ChatCltToNetConnector chatConnector;
    private Reader reader;

    public TuiHandler() {
        this.model = new MiniGameModel();

        this.tuiUpdater = new TuiUpdater(model, new Connecting());
    }

    public void start() {

        reader = new Reader(model, tuiUpdater);

        while (connector == null) {
            connector = reader.listenForConnect();
        }
        //TODO create chat connector

        while (model.myName() == null) {
            reader.listenForNick();
        }

        while (model.myName().equals(model.getGodPlayer())) {
            reader.listenForNumOfP();
        }


        while (true) {
            reader.listen();
        }


    }


}
