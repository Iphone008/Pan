//package com.example.mydome.slice;
//
//import com.example.library.PanelSwitchHelper;
//import com.example.library.interfaces.IContentScroll.ContentScrollMeasurer;
//import com.example.library.interfaces.listener.IListener.IOnKeyboardChange;
//import com.example.library.interfaces.listener.IListener.OnKeyboardStateListener;
//import com.example.library.interfaces.listener.IListener.OnPanelChangeListener;
//import com.example.library.interfaces.listener.OnKeyboardStateListenerBuilder;
//import com.example.library.view.PanelSwitchLayout;
//import com.example.library.view.panel.IPanelComponent;
//import com.example.library.view.panel.PanelView;
//import com.example.mydome.ResourceTable;
//import ohos.aafwk.ability.Ability;
//import ohos.aafwk.ability.AbilitySlice;
//import ohos.aafwk.content.Intent;
//import ohos.agp.components.Component;
//import ohos.agp.components.Image;
//import ohos.agp.components.LayoutScatter;
//import ohos.agp.window.service.Window;
//import ohos.agp.window.service.WindowManager;
//import ohos.hiviewdfx.HiLog;
//import ohos.hiviewdfx.HiLogLabel;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainAbilitySlice extends AbilitySlice {
//
//    private PanelSwitchHelper mHelper;
//    private Ability mAbility;
//    private Window mWindow;
//    static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0xD001500, "MY_TAG_MainAbilitySlice");
//    private int unfilledHeight = 0;
//    PanelSwitchLayout mPanelSwitchLayout;
//    Image emotion_btn, addbt;
//    PanelView panel_addition,panel_emotion;
//    List<Component> PanelLayoutId=new ArrayList<>();
//
//
//    @Override
//    public void onStart(Intent intent) {
//        super.onStart(intent);
//        HiLog.info(LABEL,"onStart ------");
//        super.setUIContent(ResourceTable.Layout_ability_api_auto_reset_enable_layout);
//        //构建Panel的LayoutComponent源
//        PanelLayoutId.add(LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_panel_add_layout,null,false));
//        PanelLayoutId.add(LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_panel_add_layout,null,false));
//        PanelLayoutId.add(LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_panel_add_layout,null,false));
//        init();
//
//    }
//
//    @Override
//    public void init() {
//        super.init();
//        mAbility = getAbility();
//
//        mWindow = this.getWindow();
//        mWindow.setInputPanelDisplayType(WindowManager.LayoutConfig.INPUT_ADJUST_PAN);
//
//        mPanelSwitchLayout =(PanelSwitchLayout)findComponentById(ResourceTable.Id_panel_view);
//        panel_addition=(PanelView) findComponentById(ResourceTable.Id_panel_addition);
//        panel_emotion=(PanelView) findComponentById(ResourceTable.Id_panel_emotion);
//        HiLog.info(LABEL,"onStart mPanelSwitchLayout : "+(mPanelSwitchLayout==null));
//        mPanelSwitchLayout.setPanelLayoutId(PanelLayoutId);
//        mPanelSwitchLayout.onFinishInflate();
//
//        addbt=(Image)findComponentById(ResourceTable.Id_add_btn);
//        emotion_btn=(Image)findComponentById(ResourceTable.Id_emotion_btn);
//    }
//
//    @Override
//    public void onActive() {
//        super.onActive();
//        if (mHelper == null) {
//            Boolean isnull=mPanelSwitchLayout==null?true:false;
//            HiLog.info(LABEL,"mPanelSwitchLayout 是否为空 "+isnull+",childCount:"+mPanelSwitchLayout.getChildCount());
//            mHelper = new PanelSwitchHelper.Builder(getAbility(),mPanelSwitchLayout)
//                    .addKeyboardStateListener(
//                            new OnKeyboardStateListener() {
//                                @Override
//                                public void onKeyboardChange(boolean visible, int height) {
//                                HiLog.info(LABEL,"添加  addKeyboardStateListener   ");
//                                }
//                            }
//                    )
//                    .addKeyboardStateListener(
//                            //在调用前一定要先调用该方法
//                            new OnKeyboardStateListenerBuilder(){
//                                @Override
//                                public void onKeyboardChange(IOnKeyboardChange mIonKeyboardChange) {
//                                    super.onKeyboardChange(mIonKeyboardChange);
//                                }
//                            })
//                    .addEditTextFocusChangeListener((mTextField,hasFocus)->{
//                        if (hasFocus){
//
//                        }
//                    })
//
//                    .addViewClickListener((component)->{
//                        switch(component.getId()){
//                            case ResourceTable.Id_add_btn:
//                                HiLog.info(LABEL, "Id_add_btn 点击事件。。。");
//                            case ResourceTable.Id_emotion_btn:
//                                HiLog.info(LABEL, "Id_emotion_btn 点击事件。。。");
//                            default:
//                                HiLog.info(LABEL, "default 点击事件。。。");
//                        }
//                    })
//                    .addPanelChangeListener(new OnPanelChangeListener() {
//                        @Override
//                        public void onKeyboard() {
//                            HiLog.info(LABEL, "唤起系统输入法");
//                            HiLog.info(LABEL, "addPanelChangeListener  -- onKeyboard");
//                            addbt.setSelected(false);
//                        }
//                        @Override
//                        public void onNone() {
//                            HiLog.info(LABEL, "隐藏所有面板");
//                            addbt.setSelected(false);
//                        }
//
//                        @Override
//                        public void onPanel(IPanelComponent panel) {
//                            HiLog.info(LABEL, "唤起面板");
//                            if (panel instanceof PanelView) {
//                                addbt.setSelected(false);
//                            }
//                        }
//
//                        @Override
//                        public void onPanelSizeChange(IPanelComponent panel, boolean portrait, int oldWidth, int oldHeight, int width, int height) {
//                            if (panel instanceof PanelView) {
//                                switch (((PanelView) panel).getId()) {
//                                    case ResourceTable.Id_add_btn:
//                                        break;
//                                }
//                            }
//
//                        }
//                    })
//                    .addContentScrollMeasurer(new ContentScrollMeasurer() {
//                        @Override
//                        public int getScrollDistance(int defaultDistance) {
//                            return defaultDistance - unfilledHeight;
//                        }
//                        @Override
//                        public int getScrollViewId() {
//                            return ResourceTable.Id_root_view;
//                        }
//                    })
//                    .build();
//        }
//    }
//
//
//
//    @Override
//    public void onForeground(Intent intent) {
//        super.onForeground(intent);
//    }
//
//    @Override
//    protected void onBackPressed() {
//        if (mHelper != null && mHelper.hookSystemBackByPanelSwitcher()) {
//            mHelper.hookSystemBackByPanelSwitcher();
//            return;
//        }
//        super.onBackPressed();
//    }
//}
