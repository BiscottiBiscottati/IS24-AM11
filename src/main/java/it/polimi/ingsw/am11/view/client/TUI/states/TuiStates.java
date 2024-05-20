package it.polimi.ingsw.am11.view.client.TUI.states;

import java.lang.reflect.InvocationTargetException;

public enum TuiStates {
    CONNECTING(Connecting.class),
    SETTING_NAME(SettingName.class),
    SETTING_NUM(SettingNum.class),
    WAITING(Waiting.class),
    CHOOSING_STARTER(ChoosingStrt.class),
    CHOOSING_OBJECTIVE(ChoosingObj.class),
    WAITING_FOR_TURN(WaitingForTurn.class),
    PLACING(Placing.class),
    DRAWING(Drawing.class),
    ENDED(Ended.class);

    private final Class<? extends TUIState> stateClass;

    TuiStates(Class<? extends TUIState> stateClass) {
        this.stateClass = stateClass;
    }

    public TUIState getNewState() {
        try {
            return stateClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
