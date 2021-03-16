package com.example.library.interfaces.listener;

import com.example.library.interfaces.listener.IListener.IOnKeyboardChange;
import com.example.library.interfaces.listener.IListener.OnKeyboardStateListener;

public class OnKeyboardStateListenerBuilder implements OnKeyboardStateListener {

    private IOnKeyboardChange ionKeyboardChange=null;


    public void onKeyboardChange(IOnKeyboardChange mIonKeyboardChange) {
        this.ionKeyboardChange = mIonKeyboardChange;
    }

    @Override
    public void onKeyboardChange(boolean visible, int height) {
        ionKeyboardChange.IonKeyboardChange(visible, height);
    }


}
