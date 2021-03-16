package com.example.library.view.content;

import com.example.library.Constants;
import com.example.library.interfaces.IContentScroll.ContentScrollMeasurer;
import com.example.library.interfaces.ComponentAssertion;
import com.example.library.utils.DisplayUtil;
import com.example.library.utils.PanelUtil;
import com.example.library.bean.ViewPosition;
import com.example.library.view.panel.PanelView;
import ohos.accessibility.AccessibilityEventInfo;
import ohos.agp.components.*;
import ohos.agp.text.Layout;
import ohos.agp.utils.Rect;
import ohos.agp.window.service.Window;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.TouchEvent;

import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;


/**
 * 内容区域代理
 * --------------------
 */

public class ContentContainerImpl implements ComponentAssertion, IContentContainer {
    static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0xD001400, "MY_TAG_ContentContainerImpl");
    private TextField mTextField;
    private Context context;
    private Component mResetComponent;
    private IInputAction mInputAction;
    private IResetAction mResetAction;
    private String tag;
    private TextField mPixelInputView;
    private ComponentContainer mComponentContainer;
    private boolean autoReset;
    private int editTextId;
    private int resetId;
    private HashMap<Integer, ViewPosition> map=new HashMap<>();


    public ContentContainerImpl(ComponentContainer viewGroup, Boolean mAutoReset, int mEditTextId, int mRresetId) {
        DirectionalLayout directionalLayout = (DirectionalLayout) viewGroup.getComponentAt(1);
        mTextField = (TextField) directionalLayout.findComponentById(mEditTextId);
        context = viewGroup.getContext();

        mResetComponent = viewGroup.findComponentById(mRresetId);
        tag = ContentContainerImpl.class.getSimpleName();
        mComponentContainer = viewGroup;
        autoReset = mAutoReset;
        editTextId = mEditTextId;
        resetId = mRresetId;

        HiLog.info(LABEL, "ContentContainerImpl  view end");
        init();
    }


    public void init() {
        HiLog.info(LABEL, "init: 进入 ContentContainerImpi初始化 ");
        try {
            assertComponent();
        } catch (Exception e) {
            HiLog.info(LABEL, "init:  assertView " + e.getStackTrace());
        }


        HiLog.info(LABEL, "ContentContainerImpl IResetAction Start  ");

        mResetAction = new IResetAction() {
            private Runnable action = null;
            private boolean enableReset = false;

            /**
             * 当子类不处理事件时，则 hookOnTouchEvent 会尝试消费 DOWN 。
             * 当子类处理事件时，则没有机会 hookOnTouchEvent。 这是时候有两种做法
             * 1. 寻找一个在子 View 的 hook 点，在不影响可滑动的场景先，拦截 ACTION_UP 就可以了.(Demo中)
             * 2. 如果不想在子 View 处理，则需要在点击的区域构建一个透明view盖住，监听点击之后调用 PanelSwitchHelper#resetState 手动隐藏。
             * hookDispatchTouchEvent 为第一种方案预留可能
             */
            @Override
            public boolean hookDispatchTouchEvent(TouchEvent ev, boolean consume) {
                HiLog.info(LABEL, " hookDispatchTouchEvent ev.getAction():  " + ev.getAction());
                if (ev != null) {
                    HiLog.info(LABEL, " hookDispatchTouchEvent  TouchEvent.OTHER_POINT_UP : " + TouchEvent.OTHER_POINT_UP);
                    if (ev.getAction() == TouchEvent.OTHER_POINT_UP) {
                        if (action != null) {
                            HiLog.info(LABEL, "hookDispatchTouchEvent---> autoReset  "+autoReset);
                            HiLog.info(LABEL, "hookDispatchTouchEvent---->enableReset  "+autoReset);
                            HiLog.info(LABEL, "hookDispatchTouchEvent--->!consume  "+!consume);
                            if (autoReset && enableReset && !consume) {
                                action.run();
                                HiLog.info(LABEL, "hookDispatchTouchEvent: hook ACTION_UP ");
                                return true;
                            }
                        }
                    }

                }
                return false;
            }

            /**
             * 子view不消费事件时，则默认会自己处理。
             * 当不需要指定reset区域时，捕获 ACTION_DOWN 进行消费。
             * 当指定reset区域时，则需要匹配事件发生的位置是否在区域内
             */
            @Override
            public boolean hookOnTouchEvent(TouchEvent ev) {
                HiLog.error(LABEL, "hookOnTouchEvent ev.getAction()  " + ev.getAction());
                if (ev != null) {
                    HiLog.error(LABEL, "hookOnTouchEvent ev.getAction()  " + ev.getAction());
                    if (ev.getAction() == TouchEvent.OTHER_POINT_UP) {
                        if (action != null) {
                            if (mResetComponent == null || eventInViewArea(mResetComponent, ev)) {
                                HiLog.info(LABEL, "hookOnTouchEvent: hook ACTION_DOWN");
                                action.run();
                            }
                        }
                    }
                }
                return true;
            }

            @Override
            public void enableReset(boolean enable) {
                HiLog.error(LABEL, "enableReset ----- " + enable);
                enableReset = enable;
            }

            @Override
            public void setResetCallback(Runnable runnable) {
                HiLog.info(LABEL, "IResetAction setResetCallback: 初始化 mResetAction ");
                action = runnable;
            }

            private boolean eventInViewArea(Component mComponent, TouchEvent ev) {
                if (ev != null) {
                    MmiPoint mmiPoint = ev.getPointerPosition(ev.getIndex());
                    HiLog.info(LABEL, "x ," + mmiPoint.getX() + " y " + mmiPoint.getY());
                    float x = mmiPoint.getX();
                    float y = mmiPoint.getY();
                    Rect rect = new Rect();
                    mComponent.getSelfVisibleRect(rect);
                    if (x > rect.left && x < rect.right && y >= rect.top && y <= rect.bottom) {
                        return true;
                    }
                }
                return false;
            }
        };
        HiLog.info(LABEL, "ContentContainerImpl IInputAction Start  ");
        //输入

        mInputAction = new IInputAction() {
            private static final String TAGS = "ContentContainerImpl   IInputAction ";
            private TextField mainInputView = mTextField;
            private int mainFocusIndex = -1;
            private WeakHashMap secondaryViews = new WeakHashMap<Integer, TextField>();
            private boolean secondaryViewRequestFocus = false;
            private Component.ClickedListener onClickListener = null;
            private boolean realEditViewAttach = true;
            private Integer curPanelId = Integer.MAX_VALUE;
            private boolean checkoutInputRight = true;
            private EventHandler mEventHandler = null;
            private final RequestFocusRunnable requestFocusRunnable = new RequestFocusRunnable();
            private final ResetSelectionRunnable resetSelectionRunnable = new ResetSelectionRunnable();
            boolean retrieveFocusRight_requestFocus = false;
            boolean retrieveFocusRight_resetSelection = false;

            class RequestFocusRunnable implements Runnable {
                public boolean resetSelection = false;

                @Override
                public void run() {
                    mainInputView.requestFocus();
                    if (resetSelection) {
                        new EventHandler(EventRunner.getMainEventRunner()).postTask(new Runnable() {
                            @Override
                            public void run() {
                                mainInputView.postLayout();
                            }
                        }, 100);

                    }
                    checkoutInputRight = false;
                }
            }


            class ResetSelectionRunnable implements Runnable {

                @Override
                public void run() {
                    if (mainFocusIndex != -1 && mainFocusIndex <= mainInputView.getText().length()) {
//                                mainInputView.setSelection(mainFocusIndex);
                        mainInputView.setSelected(true);
                    }
                    checkoutInputRight = false;
                }
            }


            public void giveUpFocusRight() {
                checkoutInputRight = true;
                realEditViewAttach = false;
                if (mPixelInputView.hasFocus()) {
                    mPixelInputView.clearFocus();
                }
                checkoutInputRight = false;
            }

            @Override
            public void addSecondaryInputView(TextField TexField) {
                int key = TexField.hashCode();
                if (!secondaryViews.containsKey(key)) {
                    TexField.setFocusChangedListener(new Component.FocusChangedListener() {
                        @Override
                        public void onFocusChange(Component component, boolean b) {
                            secondaryViewRequestFocus = b;
                            ;
                        }
                    });
                    secondaryViews.put(key, TexField);
                }
            }

            @Override
            public void removeSecondaryInputView(TextField textField) {
                int key = textField.hashCode();
                if (secondaryViews.containsKey(key)) {
                    secondaryViews.remove(key);
                }
            }

            @Override
            public void setEditTextClickListener(Component.ClickedListener l) {
                HiLog.info(LABEL, "run this fun");
                onClickListener = l;
//                boolean isnull = mainInputView == null ? true : false;
                HiLog.info(LABEL, "显示的" + (mainInputView == null));
                mainInputView.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        if (realEditViewAttach) {
                            onClickListener.onClick(component);
                        } else {
                            mPixelInputView.requestFocus();
                        }
                    }
                });
            }

            @Override
            public void setEditTextFocusChangeListener(Component.FocusChangedListener l) {
                mainInputView.setFocusChangedListener(new Component.FocusChangedListener() {
                    @Override
                    public void onFocusChange(Component component, boolean b) {
                        if (b) {
                            l.onFocusChange(component, b);
                        }
                    }
                });
            }

            @Override
            public void requestKeyboard() {
                TextField targetView = realEditViewAttach == true ? mainInputView : mPixelInputView;
                if (targetView.hasFocus()) {
                    //模拟点击
                    targetView.simulateClick();
                } else {
                    targetView.requestFocus();
                }
            }

            @Override
            public void hideKeyboard(boolean clearFocus) {
                //清除焦点
//                boolean c=PanelUtil.hideKeyboard(context, mainInputView);
                if (clearFocus) {
                    mainInputView.clearFocus();
                }
            }

            @Override
            public boolean showKeyboard() {
                TextField targetView = realEditViewAttach == true ? mainInputView : mPixelInputView;
                return PanelUtil.getInstance().showKeyboard(context, mainInputView);
            }

            @Override
            public TextField getFullScreenPixelInputView() {
//                mPixelInputView.setBackground(null);
                //去除多个TextField  暂时只是用一个
                HiLog.info(LABEL, "mPixelInputView  is null " + (mTextField == null));
                return mTextField;
            }

            /**
             * 对于全屏模式：
             * 1. 如果拉起的是输入法，焦点权利归属到 realEditView，重新获取焦点及重置光标
             * 2. 如果拉起的面板且面板高度大于输入法，焦点权利也归属到 realEditView，
             * 3. 其他比如隐藏面板或者面板比输入法低，焦点权利让给 mPixelInputView
             * 非全屏模式下：
             * 所有焦点权利都在 realEditView
             */

            @Override
            public void updateFullScreenParams(boolean isFullScreen, int panelId, int panelHeight) {
                HiLog.info(LABEL, "panelId == curPanelId " + (panelId == curPanelId));
                if (panelId == curPanelId) return;
                curPanelId = panelId;
                if (secondaryViewRequestFocus) {
                    secondaryViewRequestFocus = false;
                    return;
                }
                HiLog.info(LABEL,"updateFullScreenParams   isFullScreen -->"+isFullScreen);
                if (isFullScreen) {
                    if (panelId == Constants.getInstance().PANEL_KEYBOARD) {
                        boolean retrieveFocusRight_requestFocus = true;
                        boolean retrieveFocusRight_resetSelection = true;
                        retrieveFocusRight(true,true);
                    } else if (panelId != Constants.getInstance().PANEL_NONE && !PanelUtil.getInstance().isPanelHeightBelowKeyboardHeight(context, panelHeight)) {
                        retrieveFocusRight(false,true);
                    } else {
                        giveUpFocusRight();
                    }
                } else {
                    retrieveFocusRight();
                }
            }

            public void retrieveFocusRight() {

                checkoutInputRight = true;
                realEditViewAttach = true;
//                if (mPixelInputView.hasFocus()) {
//                    mPixelInputView.clearFocus();
//                }
                recycler();
                if (retrieveFocusRight_requestFocus) {
                    new EventHandler(EventRunner.getMainEventRunner()).postTask(new Runnable() {
                        @Override
                        public void run() {
                            mainInputView.postLayout();
                        }
                    }, 200);

                } else {
                    if (retrieveFocusRight_resetSelection) {
                        //光标处理  暂不支持
//                        resetSelectionRunnable.run();
                    } else {
                        checkoutInputRight = false;
                    }
                }
            }
            boolean requestFocus=false;
            boolean resetSelection=false;

            public void retrieveFocusRight(boolean requestFocus,boolean resetSelection){
                HiLog.info(LABEL,"retrieveFocusRight panelHeight  is run");
                this.requestFocus=requestFocus;
                this.resetSelection=resetSelection;
                checkoutInputRight = true;
                realEditViewAttach = true;
//                recycler();
                if (requestFocus){
                    requestFocusRunnable.resetSelection = resetSelection;
//                    mainInputView
                }else {
                    if (resetSelection) {
                        resetSelectionRunnable.run();
                    } else {
                        checkoutInputRight = false;
                    }
                }
            }

            @Override
            public void recycler() {
                if (mEventHandler != null) {
                    mEventHandler.removeTask(requestFocusRunnable);
                    mEventHandler.removeTask(resetSelectionRunnable);
                }

            }

            @Override
            public void initIInputAction() {
                mainInputView.addTextObserver(new Text.TextObserver() {
                    @Override
                    public void onTextUpdated(String s, int i, int i1, int i2) {
                        HiLog.info(LABEL,"String s ,int i, int i1, int i2  "+s+"\n"+i+"\n"+i1+"\n"+i2+"\n");
                        if (realEditViewAttach && mainInputView.hasFocus() && !checkoutInputRight) {
//                            mainFocusIndex = mainInputView.getBeginCharIndex();
                        }
                    }
                });
            }

            @Override
            public void assertView() throws Exception {
                if (mTextField == null) {
                    throw new RuntimeException("ContentContainer should set edit_view to get the editText!");
                }
            }

            @Override
            public Component findTriggerView(int id) {
                return mComponentContainer.findComponentById(id);
            }


        };
        HiLog.info(LABEL, "ContentContainerImpl mInputAction  END");
    }

    @Override
    public IInputAction getInputActionImpl() {
        return mInputAction;
    }

    @Override
    public IResetAction getResetActionImpl() {
        return mResetAction;
    }

    @Override
    public Component findTriggerView(int id) {
        return mComponentContainer.findComponentById(id);
    }

    @Override
    public void layoutContainer(int l, int t, int r, int b, List<ContentScrollMeasurer> contentScrollMeasurer, int defaultScrollHeight, boolean canScrollOutsize, boolean reset) {
        HiLog.info(LABEL, " l, t, r, b : " + l + " " + t + " " + r + " " + b);
        mComponentContainer.arrange(l, t, r, b);

        HiLog.info(LABEL, "canScrollOutsize  ：" + canScrollOutsize);
        if (!canScrollOutsize) {
            return;
        }
        for (ContentScrollMeasurer item : contentScrollMeasurer) {
            int viewId = item.getScrollViewId();
            HiLog.info(LABEL,"viewId : "+viewId );
            HiLog.info(LABEL,"map 的大小 : "+map.size());

            if (viewId != -1) {

                PanelView component = (PanelView)mComponentContainer.findComponentById(viewId);
                HiLog.info(LABEL, "Component is null ?  "+(component==null));

                ViewPosition viewPosition=null;
                if (component==null)return;
                if (map.size()==0){
                    HiLog.info(LABEL,"map  is run ");
                    viewPosition = new ViewPosition(viewId, component.getLeft(), component.getTop(), component.getRight(), component.getBottom());
                    map.put(viewId, viewPosition);
                }

                    HiLog.info(LABEL,"viewPosition "+viewPosition);
                    int willScrollDistance = 0;
                    if (reset) {
                        int viewLeft = viewPosition.getChangeL();
                        int viewTop = viewPosition.getChangeT();
                        int viewRight = viewPosition.getChangeR();
                        int viewBottom = viewPosition.getChangeB();

                        component.arrange(viewLeft, viewTop, viewRight, viewBottom);
                        viewPosition.reset();
                    } else {
                        willScrollDistance = item.getScrollDistance(defaultScrollHeight);
                        if (willScrollDistance > defaultScrollHeight) {
                            return;
                        }
                        if (willScrollDistance < 0) {
                            willScrollDistance = 0;
                        }
                        int diffY = defaultScrollHeight - willScrollDistance;
                        viewPosition.change(viewPosition.getChangeL(), viewPosition.getChangeT() + diffY, viewPosition.getChangeR(), viewPosition.getChangeB() + diffY);
                        component.arrange(viewPosition.getChangeL(), viewPosition.getChangeT(), viewPosition.getChangeR(), viewPosition.getChangeB());
                    }
                    HiLog.info(LABEL, "layoutContainer:  ContentScrollMeasurer(id " + viewId + " , defaultScrollHeight " + defaultScrollHeight + " , scrollDistance" + willScrollDistance + " reset " + reset + ") origin (l " + viewPosition.toString() + "");
                }

        }

    }

    @Override
    public void changeContainerHeight(int targetHeight) {
        ComponentContainer.LayoutConfig layoutConfig = mComponentContainer.getLayoutConfig();
        if (layoutConfig != null && layoutConfig.height != targetHeight) {
            layoutConfig.height = targetHeight;


        }
    }

    @Override
    public void assertComponent() throws Exception {
        HiLog.info(LABEL, "assertComponent is run ");
        if (mTextField == null) {
            HiLog.info(LABEL, "assertView: ContentContainer should set edit_view to get the editText!  ");
            throw new RuntimeException("ContentContainer should set edit_view to get the editText!");
        }
    }

}


