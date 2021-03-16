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
import com.example.mydome.bean.ChatInfo;
import com.example.mydome.provider.ChatProvider;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.ArrayList;
import java.util.List;

public class ContentAbilitySlice extends AbilitySlice {

    private Text mTitleText;
    private PanelSwitchLayout mPanelSwitchLayout;
    private RelativeContentContainer mRelativeContentContainer;
    private ListContainer mListContainer;
    private Image emotion_btn, addbt;

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
            case 0:
                super.setUIContent(ResourceTable.Layout_ability_content_liner);
                break;
            case 1:
                super.setUIContent(ResourceTable.Layout_ability_content_rel);
                break;
            case 2:
                super.setUIContent(ResourceTable.Layout_ability_content_liner);
                break;
            case 3:
                super.setUIContent(ResourceTable.Layout_ability_content_liner);
                break;
        }
        //构建Panel的LayoutComponent源
        PanelLayoutId.add(LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_panel_add_layout, null, false));
        PanelLayoutId.add(LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_panel_add_layout, null, false));
        PanelLayoutId.add(LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_panel_add_layout, null, false));
        initView();
    }

    private void initView() {
        mPanelSwitchLayout = (PanelSwitchLayout) findComponentById(ResourceTable.Id_panel_switch_layout);
        mRelativeContentContainer = (RelativeContentContainer) findComponentById(ResourceTable.Id_content_view);
        mTitleText = (Text) findComponentById(ResourceTable.Id_titleText);
        addbt = (Image) findComponentById(ResourceTable.Id_add_btn);
        emotion_btn = (Image) findComponentById(ResourceTable.Id_emotion_btn);

        mPanelSwitchLayout.setPanelLayoutId(PanelLayoutId);
        mPanelSwitchLayout.onFinishInflate();
        /*mPanelSwitchLayout.onFinishInflate();*/
        mRelativeContentContainer.onFinishFlate();
        switch (mType) {
            case 0:
                mTitleText.setText("线性布局");
                break;
            case 1:
                mTitleText.setText("相对布局");
                break;
            case 2:
                mTitleText.setText("帧布局");
                break;
            case 3:
                mTitleText.setText("自定义布局");
                break;
        }

        mListContainer = (ListContainer) findComponentById(ResourceTable.Id_recycler_view);
        List<ChatInfo> list = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            list.add(new ChatInfo("模拟数据第" + i + "条"));
        }
        mChatProvider = new ChatProvider(list, this);
        mListContainer.setItemProvider(mChatProvider);
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
