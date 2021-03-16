package com.example.library;

public class Constants {

    private static Constants instance;

    public static Constants getInstance() {
        if (instance == null) {
            instance = new Constants();
        }
        return instance;
    }

    /**
     * 视图相关的常量
     */
    public static final int VISIBLE = 1;//显示
    public static final int GONE = 0;//隐藏

    public final String TAG = "Constants";
    public final String KB_PANEL_PREFERENCE_NAME = "DevUtils";
    public final String KEYBOARD_HEIGHT_FOR_L = "keyboard_height_for_l";
    public final String KEYBOARD_HEIGHT_FOR_P = "keyboard_height_for_p";
    public final float DEFAULT_KEYBOARD_HEIGHT_FOR_L = 198f;
    public final float DEFAULT_KEYBOARD_HEIGHT_FOR_P = 274f;
    public final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
    public final String NAVIGATION_BAR_HEIGHT_RES_NAME = "navigation_bar_height";
    public final String DIMEN = "dimen";
    public final String ANDROID = "android";
    /**
     * panel id, custom panel (PanelView) id is panelView's triggerViewId
     * [PanelView.getTriggerViewId]
     */
    public static final int PANEL_NONE = -1;
    public static final int PANEL_KEYBOARD = 0;
    public static final float PROTECT_KEY_CLICK_DURATION = 500L;


    public static boolean DEBUG = true;

    //animationSpeed   动画速度
    public static final int ANIMATIONSPEED_SLOWEST = 300;
    public static final int ANIMATIONSPEED_slow = 250;
    public static final int ANIMATIONSPEED_standard = 200;
    public static final int ANIMATIONSPEED_fast = 150;
    public static final int ANIMATIONSPEED_fastest = 100;


    public static final int StateBarHeight=129;
    public static final int NavigationBarHeght=126;
}
