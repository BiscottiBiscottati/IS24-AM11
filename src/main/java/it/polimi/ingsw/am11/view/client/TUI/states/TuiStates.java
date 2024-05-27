package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;

public enum TuiStates {
    CONNECTING(Connecting::new),
    SETTING_NAME(SettingName::new),
    SETTING_NUM(SettingNum::new),
    WAITING(Waiting::new),
    CHOOSING_STARTER(ChoosingStrt::new),
    CHOOSING_OBJECTIVE(ChoosingObj::new),
    ENDED(Ended::new),
    WATCHING_TABLE(WatchingTable::new),
    WATCHING_FIELD(WatchingField::new);

    private final StateFactory stateFactory;

    TuiStates(StateFactory stateFactory) {
        this.stateFactory = stateFactory;
    }

    public TUIState getNewState(MiniGameModel model) {
        return stateFactory.createState(model);
    }
}
