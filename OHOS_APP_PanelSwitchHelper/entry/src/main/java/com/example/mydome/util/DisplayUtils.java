package com.example.mydome.util;

import ohos.agp.utils.Point;
import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayAttributes;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import ohos.global.configuration.Configuration;
import ohos.hiviewdfx.HiLog;
import ohos.utils.Pair;

public class DisplayUtils {
    //vp 转dp
    public static int vp2px(Context context, float vp) {
        DisplayAttributes attributes = DisplayManager.getInstance().getDefaultDisplay(context).get().getAttributes();
        return (int) (attributes.densityPixels * vp);
    }
    //获取屏幕高度
    public static int getScreenRealHeight(Context context) {
        DisplayAttributes mDisplayAttributes= DisplayManager.getInstance().getDefaultDisplay(context).get().getAttributes();
        int mheight=(int)mDisplayAttributes.height;
        return mheight;
    }
    //获取屏幕宽度
    public static int getDisplayWidthInPx(Context context) {
        Display display = DisplayManager.getInstance().getDefaultDisplay(context).get();
        Point point = new Point();
        display.getSize(point);
        return (int) point.getPointX();
    }
    //获取屏幕大小
    public static Pair<Integer, Integer> getScreenSize(Context context) {
        return new Pair<Integer, Integer>(getDisplayWidthInPx(context), getScreenRealHeight(context));
    }
    public static  Boolean isPortrait(Context context) {
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
    }

    /**
     * 包含状态栏高度 ： getRealHeight-getHeight =状态栏高度
     * @param context
     * @return
     */
    public static float getRealHeight(Context context) {
        Point point = new Point();
        DisplayManager.getInstance().getDefaultDisplay(context).get().getRealSize(point);
//        HiLog.info(LABEL, "getRealHeight=" + point.getPointY());
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
//        HiLog.info(LABEL, "getHeight=" + point.getPointY());
        return point.getPointY();
    }
    public static int getStatusBarHeight(Context context) {
        float i=getRealHeight(context)-getHeight(context);
       return (int)i;
    }
}
