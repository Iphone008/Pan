package com.example.library.interfaces.IContentScroll;
public interface PanelHeightMeasurer {

    boolean synchronizeKeyboardHeight();

    int getTargetPanelDefaultHeight();

    int getPanelTriggerId();
}