package com.example.library.view.panel;


import com.example.library.interfaces.ComponentAssertion;
import ohos.agp.components.Component;

import java.util.List;

public interface IPanelComponent extends ComponentAssertion {
    int getBindingTriggerViewId();

    boolean isTriggerViewCanToggle();

    boolean isShowing();

    void  onFinishFlate();

    void setPanelLayoutId(Integer panelLayoutId);
}
