package com.example.library.device;

import com.example.library.Constants;
import ohos.aafwk.ability.Ability;
import ohos.agp.components.Component;
import ohos.agp.window.service.Window;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import com.example.library.utils.DisplayUtil;

import java.util.logging.Logger;

public class DeviceRuntime {
    static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x00001, "MY_TAG_DeviceRuntime");
    public DeviceInfo deviceInfoP = null;
    public DeviceInfo deviceInfoL = null;
    public Boolean isNavigationBarShow = false;
    public Boolean isPortrait = false;
    public Boolean isPad = false;
    public Boolean isFullScreen = false;
    public Context mContext;
    public Window mWindow;
    public Component mComponent;
    public Boolean cache = false;

    public DeviceRuntime(Ability  ability, Window window, Component component){
        HiLog.info(LABEL, "ability.getContext() :"+(ability==null));
        HiLog.info(LABEL, "Window.window :"+(window==null));
        HiLog.info(LABEL, "component   :"+(component==null));
        this.mContext = ability.getContext();
        this.mWindow = window;
        this.mComponent = component;
        init();
    }

    @Override
    public String toString() {
        return "DeviceRuntime{" +
                "deviceInfoP=" + deviceInfoP +
                ", deviceInfoL=" + deviceInfoL +
                ", isNavigationBarShow=" + isNavigationBarShow +
                ", isPortrait=" + isPortrait +
                ", isPad=" + isPad +
                ", isFullScreen=" + isFullScreen +
                ", mContext=" + mContext +
                ", mWindow=" + mWindow +
                ", mComponent=" + mComponent +
                ", cache=" + cache +
                '}';
    }

    private void init() {
        //type 是当前设备类型  需要获取当前设备的类型。
        isPortrait= DisplayUtil.isPortrait(mContext);
        isNavigationBarShow=DisplayUtil.isNavigationBarShow(mWindow, true);

    }
    public DeviceInfo getDeviceInfoByOrientation(Component component) {
        isPortrait = DisplayUtil.isPortrait(mContext);
        isNavigationBarShow = true;//暂时状态无法获取到  底部导航栏是否存在 DisplayUtil.isNavigationBarShow(context, window)
        isFullScreen = DisplayUtil.isFullScreen(mWindow,component);
        float navigationBarHeight= Constants.NavigationBarHeght;
        int statusBarHeight=Constants.StateBarHeight;
        int toolbarH = DisplayUtil.getToolbarHeight(mContext);
        if (toolbarH == statusBarHeight) {
            toolbarH = 0;
        }
        int screenHeight =(int)DisplayUtil.getRealHeight(component.getContext());//屏幕高度
        HiLog.info(LABEL,"getDeviceInfoByOrientation screenHeight "+screenHeight);
        int screenWithoutSystemUIHeight = (int)DisplayUtil.getHeight(component.getContext());
        int screenWithoutNavigationHeight =(int)DisplayUtil.getRealHeight(component.getContext())-Constants.NavigationBarHeght;
        HiLog.info(LABEL,"DeviceRuntime  getDeviceInfoByOrientation  screenWithoutSystemUIHeight "+screenWithoutSystemUIHeight);
        HiLog.info(LABEL,"DeviceRuntime  getDeviceInfoByOrientation screenWithoutNavigationHeight "+screenWithoutNavigationHeight);
        if (isPortrait){
            deviceInfoP = new DeviceInfo(mWindow, true,
                    statusBarHeight, navigationBarHeight, toolbarH,
                    screenHeight, screenWithoutSystemUIHeight, screenWithoutNavigationHeight);
            HiLog.info(LABEL,"DeviceRuntime  deviceInfoP  "+deviceInfoP.toString());
            return deviceInfoP != null ? deviceInfoP : null;
        }else {
            deviceInfoL = new DeviceInfo(mWindow, false,
                    statusBarHeight, navigationBarHeight, toolbarH,
                    screenHeight, screenWithoutSystemUIHeight, screenWithoutNavigationHeight);
            HiLog.info(LABEL,"DeviceRuntime  deviceInfoP  "+deviceInfoL.toString());
            return deviceInfoL != null ? deviceInfoL : null;
        }
     }
    public DeviceInfo getDeviceInfoByOrientation(Component component,boolean cache){
        HiLog.info(LABEL,"DeviceRuntime  getDeviceInfoByOrientation :  is run");
        isPortrait = DisplayUtil.isPortrait(mContext);
        isNavigationBarShow = true;//暂时状态无法获取到  底部导航栏是否存在 DisplayUtil.isNavigationBarShow(context, window)
        isFullScreen = DisplayUtil.isFullScreen(mWindow,component);
        if (cache) {
            if (isPortrait && deviceInfoP != null) {
                if (deviceInfoP != null) {
                    return deviceInfoP;
                }
            } else if (!isPortrait && deviceInfoL != null) {
                if (deviceInfoL != null) {
                    return deviceInfoL;
                }
            }
        }
        float navigationBarHeight= Constants.NavigationBarHeght;
        int statusBarHeight=Constants.StateBarHeight;
        int toolbarH = DisplayUtil.getToolbarHeight(mContext);
        if (toolbarH == statusBarHeight) {
            toolbarH = 0;
        }
        int screenHeight =(int)DisplayUtil.getRealHeight(component.getContext());//屏幕高度
        HiLog.info(LABEL,"getDeviceInfoByOrientation screenHeight "+screenHeight);
        int screenWithoutSystemUIHeight = (int)DisplayUtil.getHeight(component.getContext());
        int screenWithoutNavigationHeight =(int)DisplayUtil.getRealHeight(component.getContext())-Constants.NavigationBarHeght;
        HiLog.info(LABEL,"DeviceRuntime  getDeviceInfoByOrientation  screenWithoutSystemUIHeight "+screenWithoutSystemUIHeight);
        HiLog.info(LABEL,"DeviceRuntime  getDeviceInfoByOrientation screenWithoutNavigationHeight "+screenWithoutNavigationHeight);
        if (isPortrait){
            deviceInfoP = new DeviceInfo(mWindow, true,
                    statusBarHeight, navigationBarHeight, toolbarH,
                    screenHeight, screenWithoutSystemUIHeight, screenWithoutNavigationHeight);
            return deviceInfoP != null ? deviceInfoP : null;
        }else {
            deviceInfoL = new DeviceInfo(mWindow, false,
                    statusBarHeight, navigationBarHeight, toolbarH,
                    screenHeight, screenWithoutSystemUIHeight, screenWithoutNavigationHeight);
            return deviceInfoL != null ? deviceInfoL : null;
        }
    }
}
