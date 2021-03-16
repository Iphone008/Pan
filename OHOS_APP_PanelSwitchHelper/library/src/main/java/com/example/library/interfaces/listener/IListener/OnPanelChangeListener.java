package com.example.library.interfaces.listener.IListener;

import com.example.library.view.panel.IPanelComponent;

public interface OnPanelChangeListener {
    void onKeyboard();

    void onNone();

    void onPanel(IPanelComponent panel);

    void onPanelSizeChange(IPanelComponent panel, boolean portrait, int oldWidth, int oldHeight, int width, int height);
}
