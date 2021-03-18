package com.example.mydome.slice;

import com.example.library.PanelSwitchHelper;
import com.example.library.interfaces.IContentScroll.ContentScrollMeasurer;
import com.example.library.interfaces.listener.IListener.IOnKeyboardChange;
import com.example.library.interfaces.listener.IListener.OnKeyboardStateListener;
import com.example.library.interfaces.listener.IListener.OnPanelChangeListener;
import com.example.library.interfaces.listener.OnKeyboardStateListenerBuilder;
import com.example.library.view.PanelSwitchLayout;
import com.example.library.view.panel.IPanelComponent;
import com.example.library.view.panel.PanelView;
import com.example.mydome.ResourceTable;
import com.example.mydome.anno.ChatPageType;
import com.example.mydome.scene.adapter.ChatProvider;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.window.service.Window;
import ohos.agp.window.service.WindowManager;
import ohos.global.resource.NotExistException;
import ohos.global.resource.Resource;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatAbilitySlice extends AbilitySlice {
    ListContainer listChat;
    Text titleBar;
    PixelMapElement element;
    Component rootView;
    private PanelSwitchLayout mPanelSwitchLayout;
    private PanelSwitchHelper mHelper;
    private com.example.mydome.scene.adapter.ChatProvider mAdapter;
    private Image emotion_btn, addbt;
    private Window mWindow;
    static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0xD001500, ChatAbilitySlice.class.getName());
    List<Component> PanelLayoutId = new ArrayList<>();
    private int unfilledHeight = 0;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_chat);
        mWindow = this.getWindow();
        mWindow.setInputPanelDisplayType(WindowManager.LayoutConfig.INPUT_ADJUST_PAN);

        //构建Panel的LayoutComponent源
        PanelLayoutId.add(LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_panel_add_layout, null, false));
        PanelLayoutId.add(LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_panel_add_layout, null, false));
        mPanelSwitchLayout = (PanelSwitchLayout) findComponentById(ResourceTable.Id_panel_switch_layout);
        listChat = (ListContainer) findComponentById(ResourceTable.Id_list_chat);
        mPanelSwitchLayout = (PanelSwitchLayout) findComponentById(ResourceTable.Id_panel_switch_layout);
        titleBar=(Text) findComponentById(ResourceTable.Id_title_bar);
        rootView= findComponentById(ResourceTable.Id_root);
        addbt = (Image) findComponentById(ResourceTable.Id_add_btn);
        emotion_btn = (Image) findComponentById(ResourceTable.Id_emotion_btn);

        mPanelSwitchLayout.setPanelLayoutId(PanelLayoutId);
        mPanelSwitchLayout.onFinishInflate();
        listChat.setItemProvider(new ChatProvider(getAbility(), 8));

        Resource resource = null;
        try{
            resource = getResourceManager().getResource(ResourceTable.Media_bg_gradient);
        }catch (Exception e){

        }
        element = new PixelMapElement(resource);
        int type = intent.getIntParam("type", 0);
        switch (type) {
            case ChatPageType.DEFAULT:
                titleBar.setVisibility(Component.HIDE);
                break;
            case ChatPageType.TITLE_BAR:
                titleBar.setVisibility(Component.VISIBLE);
                titleBar.setText("Activity-有标题栏");
                break;
            case ChatPageType.CUS_TITLE_BAR:
                titleBar.setVisibility(Component.VISIBLE);
                titleBar.setText("Activity-自定义标题栏");
                getWindow().setStatusBarColor(ResourceTable.Color_btn_content_text);
                break;
            case ChatPageType.COLOR_STATUS_BAR:
                getWindow().addFlags(WindowManager.LayoutConfig.MARK_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                titleBar.setVisibility(Component.VISIBLE);
                titleBar.setText("Activity-有标题栏，状态栏着色");
                try {
                    getWindow().setStatusBarColor(getResourceManager().getElement(ResourceTable.Color_colorPrimary).getColor());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NotExistException e) {
                    e.printStackTrace();
                } catch (WrongTypeException e) {
                    e.printStackTrace();
                }
                break;
            case ChatPageType.TRANSPARENT_STATUS_BAR:
                getWindow().addFlags(WindowManager.LayoutConfig.MARK_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                try {
                    getWindow().setStatusBarColor(getResourceManager().getElement(ResourceTable.Color_trans_parent).getColor());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NotExistException e) {
                    e.printStackTrace();
                } catch (WrongTypeException e) {
                    e.printStackTrace();
                }
                titleBar.setVisibility(Component.HIDE);
                rootView.setBackground(element);
                break;
            case ChatPageType.TRANSPARENT_STATUS_BAR_DRAW_UNDER:
                getWindow().addFlags(WindowManager.LayoutConfig.MARK_ALLOW_LAYOUT_COVER_SCREEN);
                try {
                    getWindow().setStatusBarColor(getResourceManager().getElement(ResourceTable.Color_trans_parent).getColor());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NotExistException e) {
                    e.printStackTrace();
                } catch (WrongTypeException e) {
                    e.printStackTrace();
                }

                titleBar.setVisibility(Component.HIDE);
                rootView.setBackground(element);
                break;
        }
    }

    @Override
    public void onActive() {
        super.onActive();
        if (mHelper == null) {
            Boolean isnull = mPanelSwitchLayout == null ? true : false;
            HiLog.info(LABEL, "mPanelSwitchLayout 是否为空 " + isnull + ",childCount:" + mPanelSwitchLayout.getChildCount());
            mHelper = new PanelSwitchHelper.Builder(getAbility(), mPanelSwitchLayout)
                    .addKeyboardStateListener(
                            new OnKeyboardStateListener() {
                                @Override
                                public void onKeyboardChange(boolean visible, int height) {
                                    HiLog.info(LABEL, "添加  addKeyboardStateListener   ");
                                }
                            }
                    )
                    .addKeyboardStateListener(
                            //在调用前一定要先调用该方法
                            new OnKeyboardStateListenerBuilder() {
                                @Override
                                public void onKeyboardChange(IOnKeyboardChange mIonKeyboardChange) {
                                    super.onKeyboardChange(mIonKeyboardChange);
                                }
                            })
                    .addEditTextFocusChangeListener((mTextField, hasFocus) -> {
                        if (hasFocus) {

                        }
                    })

                    .addViewClickListener((component) -> {
                        switch (component.getId()) {
                            case ResourceTable.Id_add_btn:
                                HiLog.info(LABEL, "Id_add_btn 点击事件。。。");
                            case ResourceTable.Id_emotion_btn:
                                HiLog.info(LABEL, "Id_emotion_btn 点击事件。。。");
                            default:
                                HiLog.info(LABEL, "default 点击事件。。。");
                        }
                    })
                    .addPanelChangeListener(new OnPanelChangeListener() {
                        @Override
                        public void onKeyboard() {
                            HiLog.info(LABEL, "唤起系统输入法");
                            HiLog.info(LABEL, "addPanelChangeListener  -- onKeyboard");
                            addbt.setSelected(false);
                        }

                        @Override
                        public void onNone() {
                            HiLog.info(LABEL, "隐藏所有面板");
                            addbt.setSelected(false);
                        }

                        @Override
                        public void onPanel(IPanelComponent panel) {
                            HiLog.info(LABEL, "唤起面板");
                            if (panel instanceof PanelView) {
                                addbt.setSelected(false);
                            }
                        }

                        @Override
                        public void onPanelSizeChange(IPanelComponent panel, boolean portrait, int oldWidth, int oldHeight, int width, int height) {
                            if (panel instanceof PanelView) {
                                switch (((PanelView) panel).getId()) {
                                    case ResourceTable.Id_add_btn:
                                        break;
                                }
                            }

                        }
                    })
                    .addContentScrollMeasurer(new ContentScrollMeasurer() {
                        @Override
                        public int getScrollDistance(int defaultDistance) {
                            return defaultDistance - unfilledHeight;
                        }

                        @Override
                        public int getScrollViewId() {
                            return ResourceTable.Id_root_view;
                        }
                    })
                    .build();
        }
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
