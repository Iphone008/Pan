package com.example.mydome.slice;

import com.example.library.PanelSwitchHelper;
import com.example.library.interfaces.IContentScroll.ContentScrollMeasurer;
import com.example.library.interfaces.listener.IListener.IOnKeyboardChange;
import com.example.library.interfaces.listener.IListener.OnKeyboardStateListener;
import com.example.library.interfaces.listener.IListener.OnPanelChangeListener;
import com.example.library.interfaces.listener.OnKeyboardStateListenerBuilder;
import com.example.library.view.PanelSwitchLayout;
import com.example.library.view.content.RelativeContentContainer;
import com.example.library.view.panel.IPanelComponent;
import com.example.library.view.panel.PanelView;
import com.example.mydome.Constants;
import com.example.mydome.ResourceTable;
import com.example.mydome.anno.ApiContentType;
import com.example.mydome.bean.ChatInfo;
import com.example.mydome.provider.ChatProvider;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.utils.Color;
import ohos.agp.window.service.Window;
import ohos.agp.window.service.WindowManager;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContentAbilitySlice extends AbilitySlice {

    private Text mTitleText;
    private PanelSwitchLayout mPanelSwitchLayout;
    private ListContainer mListContainer;
    private Image emotion_btn, addbt;

    private Window mWindow;
    private PanelSwitchHelper mHelper;
    private ChatProvider mChatProvider;
    private int mType;
    private int unfilledHeight = 0;

    List<Component> PanelLayoutId = new ArrayList<>();
    static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0xD001500, ContentAbilitySlice.class.getName());

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        mType = intent.getIntParam(Constants.KEY_CONTENT_TYPE, 0);
        switch (mType) {
            case ApiContentType.Linear:
                super.setUIContent(ResourceTable.Layout_ability_content_liner);
                break;
            case ApiContentType.Relative:
                super.setUIContent(ResourceTable.Layout_ability_content_rel);
                break;
            case ApiContentType.Frame:
                super.setUIContent(ResourceTable.Layout_ability_content_fram);
                break;
            case ApiContentType.CUS:
                super.setUIContent(ResourceTable.Layout_ability_content_cus);
                break;
        }
        //??????Panel???LayoutComponent???
        PanelLayoutId.add(LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_panel_add_layout, null, false));
        PanelLayoutId.add(LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_panel_add_layout, null, false));
        PanelLayoutId.add(LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_panel_add_layout, null, false));
        initView();
    }

    private void initView() {
        mWindow = this.getWindow();
        mWindow.setInputPanelDisplayType(WindowManager.LayoutConfig.INPUT_ADJUST_PAN);
        mPanelSwitchLayout = (PanelSwitchLayout) findComponentById(ResourceTable.Id_panel_switch_layout);
        System.out.println("???????????????????????????View ???mPanelSwitchLayout "+(mPanelSwitchLayout==null));
        mTitleText = (Text) findComponentById(ResourceTable.Id_titleText);
        addbt = (Image) findComponentById(ResourceTable.Id_add_btn);
        emotion_btn = (Image) findComponentById(ResourceTable.Id_emotion_btn);

        mPanelSwitchLayout.setPanelLayoutId(PanelLayoutId);
        mPanelSwitchLayout.onFinishInflate();
        switch (mType) {
            case ApiContentType.Linear:
                mTitleText.setText("????????????");
                break;
            case ApiContentType.Relative:
                mTitleText.setText("????????????");
                break;
            case ApiContentType.Frame:
                mTitleText.setText("?????????");
                break;
            case ApiContentType.CUS:
                mTitleText.setText("???????????????");
                break;
        }

        mListContainer = (ListContainer) findComponentById(ResourceTable.Id_recycler_view);
        List<ChatInfo> list = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            list.add(new ChatInfo("???????????????" + i + "???"));
        }
        mChatProvider = new ChatProvider(list, this);
        mListContainer.setItemProvider(mChatProvider);
    }

    @Override
    public void onActive() {
        super.onActive();
        if (mHelper == null) {
            Boolean isnull = mPanelSwitchLayout == null ? true : false;
            HiLog.info(LABEL, "mPanelSwitchLayout ???????????? " + isnull + ",childCount:" + mPanelSwitchLayout.getChildCount());
            mHelper = new PanelSwitchHelper.Builder(getAbility(), mPanelSwitchLayout)
                    .addKeyboardStateListener(
                            new OnKeyboardStateListener() {
                                @Override
                                public void onKeyboardChange(boolean visible, int height) {
                                    HiLog.info(LABEL, "??????  addKeyboardStateListener   ");
                                }
                            }
                    )
                    .addKeyboardStateListener(
                            //???????????????????????????????????????
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
                                HiLog.info(LABEL, "Id_add_btn ?????????????????????");
                            case ResourceTable.Id_emotion_btn:
                                HiLog.info(LABEL, "Id_emotion_btn ?????????????????????");
                            default:
                                HiLog.info(LABEL, "default ?????????????????????");
                        }
                    })
                    .addPanelChangeListener(new OnPanelChangeListener() {
                        @Override
                        public void onKeyboard() {
                            HiLog.info(LABEL, "?????????????????????");
                            HiLog.info(LABEL, "addPanelChangeListener  -- onKeyboard");
                            addbt.setSelected(false);
                        }

                        @Override
                        public void onNone() {
                            HiLog.info(LABEL, "??????????????????");
                            addbt.setSelected(false);
                        }

                        @Override
                        public void onPanel(IPanelComponent panel) {
                            HiLog.info(LABEL, "????????????");
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
