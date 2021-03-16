package com.example.library.interfaces.listener;


import com.example.library.interfaces.listener.IListener.*;
import com.example.library.view.panel.IPanelComponent;

public class OnPanelChangeListenerBuilder implements OnPanelChangeListener {

    //添加对应接口
    private IOnPanel_Keyboard mIOnPanel_Keyboard = null;
    private IOnPanel_None mIOnPanel_None = null;
    private IOnPanel_Panels mIOnPanel_Panels = null;
    private IOnPanel_SizeChange mIOnPanel_SizeChange = null;

    @Override
    public void onKeyboard() {
        mIOnPanel_Keyboard.IOnKeyboard();
    }

    public void onKeyboard(IOnPanel_Keyboard iOnPanel_keyboard) {
        this.mIOnPanel_Keyboard = iOnPanel_keyboard;
    }

    @Override
    public void onNone() {
        mIOnPanel_None.IOnNone();
    }

    public void onNone(IOnPanel_None iOnPanel_none) {
        this.mIOnPanel_None = iOnPanel_none;
    }

    @Override
    public void onPanel(IPanelComponent panel) {
        mIOnPanel_Panels.IOnPanels(panel);
    }

    public void onPanel(IOnPanel_Panels iOnPanel_panels) {
        this.mIOnPanel_Panels = iOnPanel_panels;
    }

    @Override
    public void onPanelSizeChange(IPanelComponent panel, boolean portrait, int oldWidth, int oldHeight, int width, int height) {
        mIOnPanel_SizeChange.IOnPanelSizeChange(panel, portrait, oldWidth, oldHeight, width, height);
    }

    public void onPanelSizeChange(IOnPanel_SizeChange iOnPanel_sizeChange) {
        this.mIOnPanel_SizeChange = iOnPanel_sizeChange;
    }

}
