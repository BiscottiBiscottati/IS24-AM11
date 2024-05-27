package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;

import java.lang.reflect.InvocationTargetException;

public enum TuiStates {
    CONNECTING(Connecting.class),
    SETTING_NAME(SettingName.class),
    SETTING_NUM(SettingNum.class),
    WAITING(Waiting.class),
    CHOOSING_STARTER(ChoosingStrt.class),
    CHOOSING_OBJECTIVE(ChoosingObj.class),
    ENDED(Ended.class),
    WATCHING_TABLE(WatchingTable.class),
    WATCHING_FIELD(WatchingField.class);

    private final Class<? extends TUIState> stateClass;

    TuiStates(Class<? extends TUIState> stateClass) {
        this.stateClass = stateClass;
    }

    public TUIState getNewState(MiniGameModel model) {
        try {
            return stateClass.getConstructor(MiniGameModel.class).newInstance(model);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
