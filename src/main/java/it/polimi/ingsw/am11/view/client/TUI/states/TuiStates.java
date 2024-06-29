package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;

public enum TuiStates {
    CONNECTING(Connecting::new),
    SETTING_NAME(SettingName::new),
    SETTING_NUM(SettingNum::new),
    WAITING(Waiting::new),
    CHOOSING_STARTER(ChoosingStrt::new),
    CHOOSING_OBJECTIVE(ChoosingObj::new),
    ENDED(Ended::new),
    WATCHING_TABLE(WatchingTable::new),
    WATCHING_FIELD(WatchingField::new),
    CHAT(Chat::new);

    private final StateFactory stateFactory;

    TuiStates(StateFactory stateFactory) {
        this.stateFactory = stateFactory;
    }

    public static void printAskLine(TUIState state) {
        switch (state.getState()) {
            case CONNECTING -> {
                Connecting conn = (Connecting) state;
                if (conn.type.isEmpty()) {
                    System.out.print(Connecting.chooseSocketOrRmi);
                } else if (conn.ip.isEmpty()) {
                    System.out.print(Connecting.chooseIp);
                } else {
                    System.out.print(Connecting.choosePort);
                }
            }
            case SETTING_NAME -> System.out.print(SettingName.askYourName);
            case SETTING_NUM -> System.out.print(SettingNum.askForNum);
            case WAITING -> System.out.print(Waiting.askLine);
            case CHOOSING_STARTER -> System.out.print(ChoosingStrt.askForSide);
            case CHOOSING_OBJECTIVE -> System.out.print(ChoosingObj.askForObj);
            case ENDED -> System.out.print(Ended.tellNothing);
            case WATCHING_TABLE -> {
                assert state.model.getCurrentTurn() != null;
                if (state.model.getCurrentTurn().equals(state.model.myName())) {
                    if (state.model.getiPlaced()) {
                        System.out.print(WatchingTable.askForCommand);
                    } else {
                        System.out.print(WatchingTable.askToSee);
                    }
                } else {
                    System.out.print(WatchingTable.askLine);
                }
            }
            case WATCHING_FIELD -> {
                assert state.model.getCurrentTurn() != null;
                if (state.model.getCurrentTurn().equals(state.model.myName())) {
                    if (state.model.getiPlaced()) {
                        System.out.print(WatchingField.askToSee);
                    } else {
                        System.out.print(WatchingField.askForCommand);
                    }
                } else {
                    System.out.print(WatchingField.askLine);
                }
            }
            case CHAT -> System.out.print(Chat.askForMsg);
        }
    }

    public @NotNull TUIState getNewState(@NotNull MiniGameModel model) {
        return stateFactory.createState(model);
    }


}
