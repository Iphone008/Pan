package com.example.library.interfaces.listener.IListener;

import ohos.agp.components.Component;
import ohos.agp.components.TextField;

public  interface OnTextFieldChangeListener {
    void onFocusChange(Component mComponent, boolean hasFocus);

}