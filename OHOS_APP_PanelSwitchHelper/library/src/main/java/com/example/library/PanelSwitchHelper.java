package com.example.library;


import com.example.library.interfaces.ContentScrollMeasurerBuilder;
import com.example.library.interfaces.IContentScroll.ContentScrollMeasurer;
import com.example.library.interfaces.IContentScroll.PanelHeightMeasurer;
import com.example.library.interfaces.PanelHeightMeasurerBuilder;
import com.example.library.interfaces.listener.IListener.OnComponentClickListener;
import com.example.library.interfaces.listener.IListener.OnKeyboardStateListener;
import com.example.library.interfaces.listener.IListener.OnPanelChangeListener;
import com.example.library.interfaces.listener.IListener.OnTextFieldChangeListener;
import com.example.library.interfaces.listener.OnEditFocusChangeListenerBuilder;
import com.example.library.interfaces.listener.OnKeyboardStateListenerBuilder;
import com.example.library.interfaces.listener.OnPanelChangeListenerBuilder;
import com.example.library.interfaces.listener.OnViewClickListenerBuilder;
import com.example.library.view.PanelSwitchLayout;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.TextField;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.service.Window;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.ArrayList;
import java.util.List;

public class PanelSwitchHelper {
    static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0xD001500, "MY_TAG_PanelSwitchHelper");
    private PanelSwitchLayout mPanelSwitchLayout;
    public boolean async = false;

    Ability mAbility;
    Component mComponent;
    private boolean hasAttachLister = false;


    public PanelSwitchHelper(Ability ability, Builder builder, boolean showKeyboard, Component component) {
        Constants.DEBUG = builder.logTrack;
        this.mComponent = component;//传入对应的数值
        this.mAbility = ability;
        if (builder.panelSwitchLayout != null) {
            mPanelSwitchLayout = builder.panelSwitchLayout;
            mPanelSwitchLayout.setContentScrollOutsizeEnable(builder.contentScrollOutsideEnable);
            mPanelSwitchLayout.setScrollMeasurers(builder.contentScrollMeasurers);
            mPanelSwitchLayout.setPanelHeightMeasurers(builder.panelHeightMeasurers);
            mPanelSwitchLayout.bindListener(builder.viewClickListeners, builder.panelChangeListeners, builder.keyboardStatusListeners, builder.editFocusChangeListeners);
            mPanelSwitchLayout.bindWindow(mAbility, builder.window, mComponent);
            //获取当前Root节点
            if (showKeyboard) {
                mPanelSwitchLayout.toKeyboardState(true);
            }
        }
    }


    public PanelSwitchHelper(Builder builder, boolean showKeyboard) {
        Constants.DEBUG = builder.logTrack;
        if (builder.panelSwitchLayout != null) {
            mAbility = builder.getAbility();
            mComponent = builder.getRootmComponent();
            mPanelSwitchLayout = builder.panelSwitchLayout;
            mPanelSwitchLayout.setContentScrollOutsizeEnable(builder.contentScrollOutsideEnable);
            mPanelSwitchLayout.setScrollMeasurers(builder.contentScrollMeasurers);
            mPanelSwitchLayout.setPanelHeightMeasurers(builder.panelHeightMeasurers);
            mPanelSwitchLayout.bindListener(builder.viewClickListeners, builder.panelChangeListeners, builder.keyboardStatusListeners, builder.editFocusChangeListeners);
            mPanelSwitchLayout.bindWindow(mAbility, builder.window, mComponent);
            if (showKeyboard) {
                mPanelSwitchLayout.toKeyboardState(true);
            }
        }
    }

    public void addSecondaryInputView(TextField editText) {
        mPanelSwitchLayout.getContentContainer().getInputActionImpl().addSecondaryInputView(editText);
    }

    public void removeSecondaryInputView(TextField editText) {
        mPanelSwitchLayout.getContentContainer().getInputActionImpl().removeSecondaryInputView(editText);
    }

    public boolean hookSystemBackByPanelSwitcher() {
        HiLog.info(LABEL, "hookSystemBackByPanelSwitcher  back value is : " + mPanelSwitchLayout.hookSystemBackByPanelSwitcher());
        return mPanelSwitchLayout.hookSystemBackByPanelSwitcher();
    }

    /**
     * 添加 Ability 接口
     *
     * @return
     */
    public Ability getAbility() {
        return mAbility;
    }

    public void setAbility(Ability ability) {
        mAbility = ability;
    }


    public void isPanelState() {
        mPanelSwitchLayout.isPanelState();
    }

    public void isKeyboardState() {
        mPanelSwitchLayout.isKeyboardState();
    }

    public void isResetState() {
        mPanelSwitchLayout.isResetState();
    }

    /**
     * 设置内容滑动
     */
    public void setContentScrollOutsideEnable(boolean enable) {
        mPanelSwitchLayout.setContentScrollOutsizeEnable(enable);
    }

    /**
     * 判断内容是否允许滑动
     */
    public void isContentScrollOutsizeEnable() {
        mPanelSwitchLayout.isContentScrollOutsizeEnable();
    }

    /**
     * 外部显示输入框
     */
    public void toKeyboardState() {

        this.async = false;
        mPanelSwitchLayout.toKeyboardState(async);
    }

    public void toKeyboardState(boolean async) {
        this.async = async;
        mPanelSwitchLayout.toKeyboardState(async);
    }


    /**
     * 外部显示面板 performClick
     */
    public void toPanelState(int triggerViewId) {
        mPanelSwitchLayout.findComponentById(triggerViewId).simulateClick();
    }

    /**
     * 隐藏输入法或者面板
     */
    public void resetState() {
        mPanelSwitchLayout.checkoutPanel(Constants.PANEL_NONE);
    }


    //----------
    public static class Builder {
        public List<OnComponentClickListener> viewClickListeners = new ArrayList<>();
        public List<OnPanelChangeListener> panelChangeListeners = new ArrayList<OnPanelChangeListener>();
        public List<OnKeyboardStateListener> keyboardStatusListeners = new ArrayList<OnKeyboardStateListener>();
        public List<OnTextFieldChangeListener> editFocusChangeListeners = new ArrayList<OnTextFieldChangeListener>();
        public List<ContentScrollMeasurer> contentScrollMeasurers = new ArrayList<>();
        public List<PanelHeightMeasurer> panelHeightMeasurers = new ArrayList<>();
        public List<Integer> panelLayoutId = new ArrayList<>();//添加的Panel的Layout Id


        public PanelSwitchLayout panelSwitchLayout = null;
        public Window window;
        public Component rootView;
        public boolean logTrack = true;
        public boolean contentScrollOutsideEnable = true;
        public static final String TAG = "Builder";
        public Context mContext;
        public boolean showKeyboard = false;
        Ability mAbility = null;
        Component mComponent = null;
        boolean mShowKeyboard = false;

        //需要传进来 当前的 rootView
        public Builder(Window window, Component component) {

            this.window = window;
            this.rootView = component;
        }

        public Builder(AbilitySlice abilitySlice, PanelSwitchLayout mComponent) {
            this.window = abilitySlice.getWindow();
            mContext = mComponent.getContext();
            rootView = mComponent;
        }

        public Builder(Ability ability, PanelSwitchLayout mComponent) {
            this.mAbility = ability;
            this.window = ability.getWindow();
            HiLog.info(LABEL, "传进来的 rootview" + (mComponent == null));
            this.panelSwitchLayout = mComponent;
        }

        public Builder(CommonDialog dialog, PanelSwitchLayout mComponent) {
            this.window = dialog.getWindow();
            this.rootView = mComponent;
        }


        /**
         * 注意 注意  注意 因为写法不一样 一定要在传入之前调用调用，换句话说，就是引用的时候就调用 暴露出来的方法
         * 比如：OnViewClickListenerBuilder  对外面就暴露出了一个内部方法 onClickBefore（）；
         *
         * @param listener
         * @return
         */
        public Builder addViewClickListener(OnComponentClickListener listener) {
            if (!viewClickListeners.contains(listener)) {
                viewClickListeners.add(listener);
            }
            return this;
        }

        public Builder addViewClickListener(OnViewClickListenerBuilder onViewClickListenerBuilder) {
            if (!viewClickListeners.contains(onViewClickListenerBuilder)) {
                viewClickListeners.add(onViewClickListenerBuilder);
            }
            return this;
        }

        public Builder addPanelChangeListener(OnPanelChangeListener listener) {
            if (!panelChangeListeners.contains(listener)) {
                panelChangeListeners.add(listener);
            }
            return this;
        }

        public Builder addPanelChangeListener(OnPanelChangeListenerBuilder listenerBuilder) {
            if (!panelChangeListeners.contains(listenerBuilder)) {
                panelChangeListeners.add(listenerBuilder);
            }
            return this;
        }

        public Builder addKeyboardStateListener(OnKeyboardStateListener listener) {
            HiLog.info(LABEL, "addKeyboardStateListener:  判断结果 " + keyboardStatusListeners.contains(listener));
            if (!keyboardStatusListeners.contains(listener)) {
                keyboardStatusListeners.add(listener);
            }
            return this;
        }

        public Builder addKeyboardStateListener(OnKeyboardStateListenerBuilder onKeyboardStateListenerBuilder) {
            if (!keyboardStatusListeners.contains(onKeyboardStateListenerBuilder)) {
                keyboardStatusListeners.add(onKeyboardStateListenerBuilder);
            }
            return this;
        }

        public Builder addEditTextFocusChangeListener(OnTextFieldChangeListener listener) {
            if (!editFocusChangeListeners.contains(listener)) {
                editFocusChangeListeners.add(listener);
            }
            return this;
        }

        public Builder addEditTextFocusChangeListener(OnEditFocusChangeListenerBuilder onEditFocusChangeListenerBuilder) {
            if (!editFocusChangeListeners.contains(onEditFocusChangeListenerBuilder)) {
                editFocusChangeListeners.add(onEditFocusChangeListenerBuilder);
            }
            return this;
        }

        public Builder addContentScrollMeasurer(ContentScrollMeasurer scrollMeasurer) {
            if (!contentScrollMeasurers.contains(scrollMeasurer)) {
                contentScrollMeasurers.add(scrollMeasurer);
            }
            return this;
        }

        public Builder addContentScrollMeasurer(ContentScrollMeasurerBuilder contentScrollMeasurerBuilder) {
            if (!contentScrollMeasurers.contains(contentScrollMeasurerBuilder)) {
                contentScrollMeasurers.add(contentScrollMeasurerBuilder);
            }
            return this;
        }

        public Builder addPanelHeightMeasurer(PanelHeightMeasurer panelHeightMeasurer) {
            if (!panelHeightMeasurers.contains(panelHeightMeasurer)) {
                panelHeightMeasurers.add(panelHeightMeasurer);
            }
            return this;
        }

        public Builder addPanelHeightMeasurer(PanelHeightMeasurerBuilder panelHeightMeasurerBuilder) {
            if (!panelHeightMeasurers.contains(panelHeightMeasurerBuilder)) {
                panelHeightMeasurers.add(panelHeightMeasurerBuilder);
            }
            return this;
        }

        public Builder contentScrollOutsideEnable(boolean contentScrollOutsideEnable) {
            this.contentScrollOutsideEnable = contentScrollOutsideEnable;
            return this;
        }

        //添加 Ability 的位置，对PanelSwitch暴露方法
        public Ability getAbility() {
            return mAbility;
        }

        public Component getRootmComponent() {
            return panelSwitchLayout;
        }

        public Builder logTrack(boolean logTrack) {

            this.logTrack = logTrack;
            return this;
        }


        //重新整改
        public PanelSwitchHelper build(Boolean showKeyboard) {
            findSwitchLayout(rootView);
            if (panelSwitchLayout == null) {
                HiLog.info(LABEL, "build: not found PanelSwitchLayout! 1 ");
            }
            return new PanelSwitchHelper(this, showKeyboard);
        }


        public PanelSwitchHelper build() {
            findSwitchLayout(rootView);
            if (panelSwitchLayout == null) {
                HiLog.info(LABEL, "build: not found PanelSwitchLayout! 2");
            }
            return new PanelSwitchHelper(mAbility, this, mShowKeyboard, mComponent);
        }

        private void findSwitchLayout(Component view) {
            if (view instanceof PanelSwitchLayout) {
                if (panelSwitchLayout == null) {
                    HiLog.info(LABEL, "PanelSwitchHelper build : rootView has one more panelSwitchLayout!");
                }
                panelSwitchLayout = (PanelSwitchLayout) view;
                HiLog.info(LABEL, "findSwitchLayout:  创建 panelSwitchLayout");
                return;
            }

            if (view instanceof ComponentContainer) {
                HiLog.info(LABEL, "findSwitchLayout:   Views shi  ViewGroup");
                int childCount = ((ComponentContainer) view).getChildCount();
                for (int i = 0; i < childCount; i++) {
                    findSwitchLayout(((ComponentContainer) view).getComponentAt(i));
                }
            }
        }


    }

}

