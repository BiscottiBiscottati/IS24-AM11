package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.chat.ClientChatController;
import it.polimi.ingsw.am11.network.ChatCltToNetConnector;
import it.polimi.ingsw.am11.network.CltToNetConnector;
import it.polimi.ingsw.am11.view.client.TUI.states.TuiStates;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;

import java.io.IOException;

public class TuiHandler {

    private final MiniGameModel model;

    private final TuiUpdater tuiUpdater;
    private CltToNetConnector connector;
    private ClientChatController chatController;
    private ChatCltToNetConnector chatConnector;
    private ReaderV2 reader;

    public TuiHandler() {
        this.model = new MiniGameModel();

        this.tuiUpdater = new TuiUpdater(model, TuiStates.CONNECTIONG);
    }

    public void start() throws IOException {

        reader = new ReaderV2(model, tuiUpdater);

        while (true) {
            reader.listen();
        }


    }


}
