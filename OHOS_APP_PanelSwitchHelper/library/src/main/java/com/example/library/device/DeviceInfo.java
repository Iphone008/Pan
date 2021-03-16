package com.example.library.device;

import ohos.agp.window.service.Window;

public class DeviceInfo {
    private Window window;
    private Boolean isPortrait;
    private int statusBarH;
    private float navigationBarH;
    private int toolbarH;
    private int screenH;
    private int screenWithoutSystemUiH;
    private float screenWithoutNavigationH;

    public DeviceInfo() {
        super();
    }

    public DeviceInfo(Window window, Boolean isPortrait, int statusBarH, float navigationBarH, int toolbarH, int screenH, int screenWithoutSystemUiH, float screenWithoutNavigationH) {
        this.window = window;
        this.isPortrait = isPortrait;
        this.statusBarH = statusBarH;
        this.navigationBarH = navigationBarH;
        this.toolbarH = toolbarH;
        this.screenH = screenH;
        this.screenWithoutSystemUiH = screenWithoutSystemUiH;
        this.screenWithoutNavigationH = screenWithoutNavigationH;
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public Boolean getPortrait() {
        return isPortrait;
    }

    public void setPortrait(Boolean portrait) {
        isPortrait = portrait;
    }

    public int getStatusBarH() {
        return statusBarH;
    }

    public void setStatusBarH(int statusBarH) {
        this.statusBarH = statusBarH;
    }

    public float getNavigationBarH() {
        return navigationBarH;
    }

    public void setNavigationBarH(float navigationBarH) {
        this.navigationBarH = navigationBarH;
    }

    public int getToolbarH() {
        return toolbarH;
    }

    public void setToolbarH(int toolbarH) {
        this.toolbarH = toolbarH;
    }

    public int getScreenH() {
        return screenH;
    }

    public void setScreenH(int screenH) {
        this.screenH = screenH;
    }

    public int getScreenWithoutSystemUiH() {
        return screenWithoutSystemUiH;
    }

    public void setScreenWithoutSystemUiH(int screenWithoutSystemUiH) {
        this.screenWithoutSystemUiH = screenWithoutSystemUiH;
    }

    public float getScreenWithoutNavigationH() {
        return screenWithoutNavigationH;
    }

    public void setScreenWithoutNavigationH(int screenWithoutNavigationH) {
        this.screenWithoutNavigationH = screenWithoutNavigationH;
    }

    public float getCurrentNavigationBarHeightWhenVisible(Boolean isPortrait, Boolean isPad) {
        if (isPortrait) {
            return navigationBarH;
        } else {
            if (isPad) {
                return navigationBarH;
            } else {
                return 0;
            }
        }
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "window=" + window +
                ", isPortrait=" + isPortrait +
                ", statusBarH=" + statusBarH +
                ", navigationBarH=" + navigationBarH +
                ", toolbarH=" + toolbarH +
                ", screenH=" + screenH +
                ", screenWithoutSystemUiH=" + screenWithoutSystemUiH +
                ", screenWithoutNavigationH=" + screenWithoutNavigationH +
                '}';
    }
}
