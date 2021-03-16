package com.example.library.utils;

import com.example.library.Constants;
import ohos.aafwk.ability.Ability;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.render.render3d.ViewHolder;
import ohos.agp.render.render3d.resources.ResourceManager;
import ohos.agp.utils.Point;
import ohos.agp.utils.Rect;
import ohos.agp.window.service.*;
import ohos.app.Context;
import ohos.global.configuration.Configuration;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.lang.reflect.Method;

//package ohos.agp.utils;

public class DisplayUtil {
    static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0xD001400, "MY_TAG_DisplayUtil");
    private static DisplayUtil instance;

    public static DisplayUtil getInstance() {
        if (instance == null) {
            instance = new DisplayUtil();
        }
        return instance;
    }


    /**isPortrait 添加方向
     * @param context
     * @return
     */
    //水平方向=1；      DIRECTION_HORIZONTAL = 1;
    //方向_未定义=-1； DIRECTION_UNDEFINED = -1;
    //
    //垂直方向=0； DIRECTION_VERTICAL = 0;

    public static Boolean isPortrait(Context context) {
        if (context!=null) {
            Configuration mConfiguration= context.getResourceManager().getConfiguration();
            Boolean DIRECTION_TYPE=false;
            switch (mConfiguration.direction){
                //水平方向
                case  Configuration.DIRECTION_HORIZONTAL:
                    DIRECTION_TYPE=false;
                    break;
                //垂直方向
                case  Configuration.DIRECTION_VERTICAL:
                    DIRECTION_TYPE=true;
                    break;

            }
            return  DIRECTION_TYPE;
        }else {
            return  false;
        }

    }


    /**
     * 处理导航栏是否显示
     *
     * @param mWindow
     * @param ShowToolBar
     */
    public static boolean isNavigationBarShow(Window mWindow, Boolean ShowToolBar) {
        boolean visible=true;
//        if (ShowToolBar) {
//            WindowManager.getInstance().getTopWindow().get().setStatusBarVisibility(Constants.VISIBLE);
//            visible=true;
//            HiLog.debug(LABEL, "显示toolbar ");
//        } else {
//            //隐藏title
//            mWindow.addFlags(WindowManager.LayoutConfig.MARK_FULL_SCREEN);
//            visible=false;
//            HiLog.debug(LABEL, "隐藏toolbar ");
//        }
        return visible;
    }
    /**
     * 判断虚拟按键是否存在
     */
//    public static boolean hasNavBar(Ability mAbility) {
//        boolean hasMenuKey = ViewConfiguration.get(activity)
//                .hasPermanentMenuKey();
//        boolean hasBackKey = KeyCharacterMap
//                .deviceHasKey(KeyEvent.KEYCODE_BACK);
//
//        if (!hasMenuKey && !hasBackKey) {
//            // 没有虚拟按键返回 true
//            return true;
//        }
//        // 有虚拟按键返回 false
//        return false;
//    }

    /**
     * 状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
       return (int) (getRealHeight(context)-getHeight(context));
    }

    /**
     * 获取屏幕高度，不包含状态栏的高度
     *
     * @param context 上下文
     * @return 屏幕高度，不包含状态栏的高度
     */
    public static float getDisplayHeightInPx(Context context) {
        Display display = DisplayManager.getInstance().getDefaultDisplay(context).get();
        Point point = new Point();
        display.getSize(point);
        return point.getPointY();
    }


    /**
     * 获取toolar的高度，但是这个方法仅仅在非沉浸下才有用。
     *
     * @param
     * @return
     */
    public static int getToolbarHeight(Context context) {
        int number =20;
        return number;
    }

    /**
     * 获取屏幕高度  包含状态栏,
     * @param context
     * @return
     */
    public static int getScreenRealHeight(Context context) {
        DisplayAttributes mDisplayAttributes= DisplayManager.getInstance().getDefaultDisplay(context).get().getAttributes();
        int mheight=(int)mDisplayAttributes.height;
        return mheight;
    }

    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int getDisplayWidthInPx(Context context) {
        Display display = DisplayManager.getInstance().getDefaultDisplay(context).get();
        Point point = new Point();
        display.getSize(point);
        return (int) point.getPointX();
    }
    /**
     * vp转像素
     *
     * @param context
     * @param vp
     * @return
     */
    public static int vp2px(Context context, float vp) {
        DisplayAttributes attributes = DisplayManager.getInstance().getDefaultDisplay(context).get().getAttributes();
        return (int) (attributes.densityPixels * vp);
    }
    //判断当前是否是浸入式 全屏的开发模式
    public static boolean isFullScreen(Window mWindow,Component component) {

        if (mWindow.getStatusBarVisibility()==Component.HIDE){
            return true;
        }else {
            return false;
        }
    }

    public static  int[] getLocationOnScreen(Component mComponent){
        return mComponent.getLocationOnScreen();
    }
    /**
     * 包含状态栏的高度
     *
     * @param context
     */
    public static float getRealHeight(Context context) {
        Point point = new Point();
        if (DisplayManager.getInstance().getDefaultDisplay(context)==null)return 0;
        DisplayManager.getInstance().getDefaultDisplay(context).get().getRealSize(point);
        return point.getPointY();
    }

    /**
     * 不包含状态栏的高度
     *
     * @param context
     */
    public static float getHeight(Context context) {
        Point point = new Point();
        DisplayManager.getInstance().getDefaultDisplay(context).get().getSize(point);
        return point.getPointY();
    }
    //显示键盘
    public static boolean showSoftInput(){
        try {
            Class inputClass = Class.forName("ohos.miscservices.inputmethod.InputMethodController");
            Method method = inputClass.getMethod("getInstance");
            Object object = method.invoke(new Object[]{});
            Method startInput = inputClass.getMethod("startInput",int.class,boolean.class);
            return (boolean)startInput.invoke(object,1,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 隐藏键盘
     * @return
     */
    public static boolean hideSoftInput(){
        try {
            Class inputClass = Class.forName("ohos.miscservices.inputmethod.InputMethodController");
            Method method = inputClass.getMethod("getInstance");
            Object object = method.invoke(new Object[]{});
            Method stopInput = inputClass.getMethod("stopInput",int.class);
            return (boolean)stopInput.invoke(object,1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



}
