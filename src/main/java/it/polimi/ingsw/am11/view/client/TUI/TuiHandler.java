package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.chat.ClientChatController;
import it.polimi.ingsw.am11.network.ChatCltToNetConnector;
import it.polimi.ingsw.am11.network.CltToNetConnector;
import it.polimi.ingsw.am11.view.client.TUI.states.TuiStates;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;

import java.io.IOException;

// This class is basically the main class that initialize the tui

public class TuiHandler {

    private final MiniGameModel model;

    private final TuiUpdater tuiUpdater;
    private CltToNetConnector connector;
    private ClientChatController chatController;
    private ChatCltToNetConnector chatConnector;
    private ReaderV2 reader;

    public TuiHandler() {
        this.model = new MiniGameModel();

        this.tuiUpdater = new TuiUpdater(model, TuiStates.CONNECTING);
    }

    public void start() throws IOException {

        ConsUtils.clear();

        System.out.println("""

                                   oooooo   oooooo     oooo           oooo                                                           .                             \s
                                    `888.    `888.     .8'            `888                                                         .o8                             \s
                                     `888.   .8888.   .8'    .ooooo.   888   .ooooo.   .ooooo.  ooo. .oo.  .oo.    .ooooo.       .o888oo  .ooooo.                  \s
                                      `888  .8'`888. .8'    d88' `88b  888  d88' `"Y8 d88' `88b `888P"Y88bP"Y88b  d88' `88b        888   d88' `88b                 \s
                                       `888.8'  `888.8'     888ooo888  888  888       888   888  888   888   888  888ooo888        888   888   888                 \s
                                        `888'    `888'      888    .o  888  888   .o8 888   888  888   888   888  888    .o        888 . 888   888  .o.   .o.   .o.\s
                                         `8'      `8'       `Y8bod8P' o888o `Y8bod8P' `Y8bod8P' o888o o888o o888o `Y8bod8P'        "888" `Y8bod8P'  Y8P   Y8P   Y8P\s
                                                                                                                                                                   \s
                                                                                                                                                                   \s
                                                                                                                                                                   \s
                                                                   
                                   """);

        System.out.println("""
                                                                      
                                     .oooooo.                   .o8                             ooooo      ooo               .                                  oooo   o8o          \s
                                    d8P'  `Y8b                 "888                             `888b.     `8'             .o8                                  `888   `"'          \s
                                   888           .ooooo.   .oooo888   .ooooo.  oooo    ooo       8 `88b.    8   .oooo.   .o888oo oooo  oooo  oooo d8b  .oooo.    888  oooo   .oooo.o\s
                                   888          d88' `88b d88' `888  d88' `88b  `88b..8P'        8   `88b.  8  `P  )88b    888   `888  `888  `888""8P `P  )88b   888  `888  d88(  "8\s
                                   888          888   888 888   888  888ooo888    Y888'          8     `88b.8   .oP"888    888    888   888   888      .oP"888   888   888  `"Y88b. \s
                                   `88b    ooo  888   888 888   888  888    .o  .o8"'88b         8       `888  d8(  888    888 .  888   888   888     d8(  888   888   888  o.  )88b\s
                                    `Y8bood8P'  `Y8bod8P' `Y8bod88P" `Y8bod8P' o88'   888o      o8o        `8  `Y888""8o   "888"  `V88V"V8P' d888b    `Y888""8o o888o o888o 8""888P'\s
                                                                                                                                                                                    \s
                                                                                                                                                                                    \s
                                                                                                                                                                                    \s
                                   """);
        System.out.println("by Osama Atiqi, Edoardo Bergamo, Ferdinando Cioffi and Zining Chen");
        printLoadingBar(3);
        ConsUtils.clear();
        reader = new ReaderV2(tuiUpdater);

        tuiUpdater.getCurrentTuiState().restart(false, null);

        while (true) {
            reader.listen();
        }


    }

    public static void printLoadingBar(int durationInSeconds) {
        int totalSteps = 100; // number of steps in the loading bar
        int stepDuration =
                durationInSeconds * 1000 / totalSteps; // duration of each step in milliseconds

        for (int i = 0; i < totalSteps; i++) {
            try {
                Thread.sleep(stepDuration); // wait for the step duration
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print("#"); // print a part of the loading bar
        }

    }

}

