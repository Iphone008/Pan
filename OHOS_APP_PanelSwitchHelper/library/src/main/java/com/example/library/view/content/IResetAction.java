package com.example.library.view.content;


import ohos.multimodalinput.event.TouchEvent;

public interface IResetAction {
    void enableReset(boolean enable);

    void setResetCallback(Runnable runnable);

    boolean hookDispatchTouchEvent(TouchEvent ev, boolean consume);

    boolean hookOnTouchEvent(TouchEvent ev);
}
