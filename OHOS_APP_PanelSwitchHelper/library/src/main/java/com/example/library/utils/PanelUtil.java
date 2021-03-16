package com.example.library.utils;


import com.example.library.Constants;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.Component;
import ohos.app.Context;
import ohos.data.DatabaseHelper;
import ohos.data.preferences.Preferences;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;



public class PanelUtil {
    static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0xD001400, "MY_TAG_PanelUtil");
    private static int pHeight = -1;
    private static int lHeight = -1;
    private static PanelUtil instance;
    private static Preferences mPreferences;
    private static DatabaseHelper mDatabaseHelper;

    public static PanelUtil getInstance() {
        if (instance == null) {
            instance = new PanelUtil();
        }
        return instance;
    }

    //    清除数据
    public static void clearData(Context context) {
        PreferencesIsnull(mDatabaseHelper,context);
        pHeight = -1;
        lHeight = -1;
        mPreferences.putInt(Constants.getInstance().KB_PANEL_PREFERENCE_NAME, Context.MODE_PRIVATE);
        mPreferences.flush();
    }

//    public boolean showKeyboard(Context context, Component mComponent ) {
//        mComponent.requestFocus();
////        InputMethodManager methodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
////        return methodManager.showSoftInput(view, 0);
//
//        return false;
//
//    }
//
//    public boolean hideKeyboard(Context context, View view) {
//        InputMethodManager mInputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        return mInputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }

    public int getKeyBoardHeight(Context context) {
        HiLog.debug(LABEL, "getKeyBoardHeight:  is run ");
        PreferencesIsnull(mDatabaseHelper,context);
        boolean isPortrait = DisplayUtil.getInstance().isPortrait(context);
        HiLog.debug(LABEL, "getKeyBoardHeight:  isPortrait "+isPortrait);
        if (isPortrait && pHeight != -1) {
            return pHeight;
        }
        if (!isPortrait && lHeight != -1) {
            return lHeight;
        }
        String key = isPortrait == true ? Constants.getInstance().KEYBOARD_HEIGHT_FOR_P : Constants.getInstance().KEYBOARD_HEIGHT_FOR_L;
        HiLog.info(LABEL,"panelUtils   is null -->context  :  "+(context==null));
        HiLog.info(LABEL,"panelUtils   is null -->isPortrait  :  "+isPortrait);
        HiLog.info(LABEL,"panelUtils   is null -->context  :  "+context);
        int defaultHeight = DisplayUtil.vp2px(context, isPortrait == true ? Constants.getInstance().DEFAULT_KEYBOARD_HEIGHT_FOR_P : Constants.getInstance().DEFAULT_KEYBOARD_HEIGHT_FOR_L);
        HiLog.info(LABEL,"defaultHeight  "+defaultHeight);
        int result = mPreferences.getInt(key, defaultHeight);
        if (result != defaultHeight) {
            if (isPortrait) {
                pHeight = result;

            } else {
                lHeight = result;
            }

        }
        return result;

    }

    public boolean isPanelHeightBelowKeyboardHeight(Context context, int curPanelHeight) {
        return hasMeasuredKeyboard(context) && getKeyBoardHeight(context) > curPanelHeight ? true : false;
    }

    public boolean setKeyBoardHeight(AbilitySlice mAbility , int height) {
        PreferencesIsnull(mDatabaseHelper,mAbility);
        if (getKeyBoardHeight(mAbility) == height) {
            HiLog.info(LABEL,"current KeyBoardHeight is equal，just ignore！");
            return false;
        }
        boolean isPortrait = DisplayUtil.getInstance().isPortrait(mAbility);
        if (isPortrait && pHeight == height) {
            HiLog.info(LABEL,"current KeyBoardHeight is equal，just ignore！");
            return false;
        }
        if (!isPortrait && lHeight == height) {
            HiLog.info(LABEL,"current KeyBoardHeight is equal，just ignore！");
            return false;
        }
        String key = isPortrait == true ? Constants.getInstance().KEYBOARD_HEIGHT_FOR_P : Constants.getInstance().KEYBOARD_HEIGHT_FOR_L;
        mPreferences.putInt(key, height).flush();
        if (isPortrait) {
            pHeight = height;
        } else {
            lHeight = height;
        }
        //如果没有保存报错   则返回true
        return true;
    }

    public boolean hasMeasuredKeyboard(Context context) {
        HiLog.info(LABEL,"hasMeasuredKeyboard is run ");
        getKeyBoardHeight(context);
        return pHeight != -1 || lHeight != -1;
    }
    //判断 mDatabaseHelper 实力不存在 创建
    public static void PreferencesIsnull(DatabaseHelper mDatabaseHelper, Context context){
        if (mDatabaseHelper==null&&context!=null){
            mDatabaseHelper= new DatabaseHelper(context);
            mPreferences = mDatabaseHelper.getPreferences(Constants.getInstance().KB_PANEL_PREFERENCE_NAME);
        }
    }
    //显示和隐藏 键盘
    public boolean showKeyboard(Context context, Component view) {
//        view.requestFocus();
//        InputMethodManager methodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        return methodManager.showSoftInput(view, 0);
        return true;
    }

    public boolean hideKeyboard(Context context, Component view) {
//        InputMethodManager mInputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        return mInputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        return true;
    }
}
