package com.example.library.view.panel;


import com.example.library.interfaces.ViewAssertion;
import ohos.agp.components.StackLayout;

public interface IPanelView extends ViewAssertion {
    int getBindingTriggerViewId();

    boolean isTriggerViewCanToggle();

    boolean isShowing();
}
