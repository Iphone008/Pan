package com.example.library.interfaces.listener.IListener;

import com.example.library.view.panel.IPanelComponent;
/**
 * IOnPanel_SizeChange  是 OnPanelChangeListenerBuilder需要进行引用
 */
public interface IOnPanel_SizeChange {
    public void IOnPanelSizeChange(IPanelComponent iPanelComponent, boolean portrait, int oldWidth, int oldHeight, int width, int height);
}
